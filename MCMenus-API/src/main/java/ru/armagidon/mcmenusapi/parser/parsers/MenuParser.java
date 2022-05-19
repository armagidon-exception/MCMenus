package ru.armagidon.mcmenusapi.parser.parsers;

import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.menu.Menu;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.parser.ElementParser;
import ru.armagidon.mcmenusapi.parser.ElementParsingContext;
import ru.armagidon.mcmenusapi.parser.tags.*;
import ru.armagidon.mcmenusapi.style.ElementStyle;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MenuParser
{

    private static final Map<Class<? extends Annotation>, ElementParser<?>> parsers = new ConcurrentHashMap<>();

    static {
        parsers.put(LinkTag.class, new LinkParser());
        parsers.put(ButtonTag.class, new ButtonParser());
        parsers.put(CheckBoxTag.class, new CheckBoxParser());
    }

    public static MenuPanel convert(Menu owner, String id, Object dataModel) {
        //Basic setup for menu
        MenuPanel defaultPanel = owner.createPanelAndAdd(id);

        if (dataModel.getClass().isAnnotationPresent(TitlePath.class)) {
            TitlePath title = dataModel.getClass().getAnnotation(TitlePath.class);
            if (title.isPath()) {
                defaultPanel.getStyleSheet().getHeadStyle().getMenuTitle().set(MCMenusAPI.getTitleRegistry().getByPath(title.title()));
            } else
                defaultPanel.getStyleSheet().getHeadStyle().getMenuTitle().set(title.title());
        }
        if (dataModel.getClass().isAnnotationPresent(LookAndFeel.class)) {
            LookAndFeel lookAndFeel = dataModel.getClass().getAnnotation(LookAndFeel.class);
            defaultPanel.getStyleSheet().getHeadStyle().setMenuSize(lookAndFeel.size());
            defaultPanel.getStyleSheet().getHeadStyle().setMenuLookType(lookAndFeel.lookType());
        }

        //Parse tags
        //Methods
        var methods = dataModel.getClass().getDeclaredMethods();
        parsers.entrySet().stream().filter(e -> e.getValue().mayBeAttachedTo().equals(ElementType.METHOD))
                .forEach(entry -> Arrays.stream(methods)
                        .filter(m -> m.getParameterCount() == 1)
                        .filter(m -> m.getParameterTypes()[0].equals(Player.class))
                        .filter(m -> m.isAnnotationPresent(entry.getKey()))
                        .forEach(m -> parseAndAddElement(ElementParsingContext.createContext(m, dataModel, owner, owner.getViewer()), defaultPanel, (ElementParser<? super Method>) entry.getValue())));

        //Filter field based elements with primitive types(complex types will be used to create links)
        var fields = dataModel.getClass().getDeclaredFields();
        parsers.entrySet().stream().filter(e -> e.getValue().mayBeAttachedTo().equals(ElementType.FIELD))
                .filter(e -> !e.getKey().equals(LinkTag.class))
                .forEach(entry -> Arrays.stream(fields)
                        .filter(f -> f.getType().isPrimitive() || f.getType().equals(String.class))
                        .filter(f -> f.isAnnotationPresent(entry.getKey()))
                        .filter(f -> entry.getValue().supportedTypes().length == 0 || Arrays.stream(entry.getValue().supportedTypes()).anyMatch(type -> f.getType().equals(type)))
                        .forEach(f -> parseAndAddElement(ElementParsingContext.createContext(f, dataModel, owner, defaultPanel.getId()), defaultPanel, (ElementParser<? super Field>) entry.getValue())));

        ElementParser<Field> linkParser = (ElementParser<Field>) parsers.get(LinkTag.class);
        Arrays.stream(fields)
                .filter(f -> !f.getType().isPrimitive() && !f.getType().equals(String.class))
                .filter(f -> f.isAnnotationPresent(LinkTag.class))
                .filter(f -> linkParser.supportedTypes().length == 0 || Arrays.stream(linkParser.supportedTypes()).anyMatch(type -> f.getType().equals(type)))
                .forEach(f -> parseAndAddElement(ElementParsingContext.createContext(f, dataModel, owner),
                        defaultPanel, linkParser));

        owner.setModelFor(defaultPanel, dataModel);
        return defaultPanel;
    }

    private static <I extends AccessibleObject> void parseAndAddElement(ElementParsingContext<I> input, MenuPanel defaultPanel, ElementParser<I> parser) {
        MenuElement element = parser.parse(input);
        ElementStyle style = parser.parseStyle(input.getInput());
        defaultPanel.addElement(element, style);
    }
}
