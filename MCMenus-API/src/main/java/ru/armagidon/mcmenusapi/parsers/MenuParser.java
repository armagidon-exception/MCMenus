package ru.armagidon.mcmenusapi.parsers;

import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.elements.Link;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.menu.Menu;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.misc.jool.Unchecked;
import ru.armagidon.mcmenusapi.parsers.tags.LinkTag;
import ru.armagidon.mcmenusapi.parsers.tags.LookAndFeel;
import ru.armagidon.mcmenusapi.parsers.tags.TitlePath;
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
        parsers.entrySet().stream().filter(e -> e.getValue().supportedType().equals(ElementType.METHOD))
                .forEach(entry -> Arrays.stream(methods)
                        .filter(m -> m.getParameterCount() == 1)
                        .filter(m -> m.getParameterTypes()[0].equals(Player.class))
                        .filter(m -> m.isAnnotationPresent(entry.getKey()))
                        .forEach(m -> parseAndAddElement(m, defaultPanel, (ElementParser<? super Method>) entry.getValue())));

        //Filter field based elements with primitive types(complex types will be used to create links)
        var fields = dataModel.getClass().getDeclaredFields();
        parsers.entrySet().stream().filter(e -> e.getValue().supportedType().equals(ElementType.FIELD))
                .filter(e -> !e.getKey().equals(LinkTag.class))
                .forEach(entry -> Arrays.stream(fields)
                        .filter(f -> f.getType().isPrimitive() || f.getType().equals(String.class))
                        .filter(f -> f.isAnnotationPresent(entry.getKey()))
                        .forEach(f -> parseAndAddElement(f, defaultPanel, (ElementParser<? super Field>) entry.getValue())));

        ElementParser<Field> linkParser = (ElementParser<Field>) parsers.get(LinkTag.class);
        Arrays.stream(fields).filter(f -> f.isAnnotationPresent(LinkTag.class)).forEach(Unchecked.consumer(f -> {
            Link element = (Link) linkParser.parse(f);
            element.setLink(convert(owner, f.getName(), f.get(dataModel)));
            ElementStyle style = linkParser.parseStyle(f);
            defaultPanel.addElement(element, style);
        }));

        owner.setModelFor(defaultPanel, dataModel);
        return defaultPanel;
    }

    private static <I extends AccessibleObject> void parseAndAddElement(I input, MenuPanel defaultPanel, ElementParser<I> parser) {
        MenuElement element = parser.parse(input);
        ElementStyle style = parser.parseStyle(input);
        defaultPanel.addElement(element, style);
    }
}
