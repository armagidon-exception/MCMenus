package ru.armagidon.mcmenusapi.parser.parsers;

import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.data.ElementParsingContext;
import ru.armagidon.mcmenusapi.data.StyleParsingContext;
import ru.armagidon.mcmenusapi.menu.Menu;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.menu.elements.DivisionBlock;
import ru.armagidon.mcmenusapi.menu.elements.MenuElement;
import ru.armagidon.mcmenusapi.menu.layout.Layout;
import ru.armagidon.mcmenusapi.menu.layout.Position;
import ru.armagidon.mcmenusapi.misc.Reflection;
import ru.armagidon.mcmenusapi.misc.jool.StreamUtils;
import ru.armagidon.mcmenusapi.misc.jool.Unchecked;
import ru.armagidon.mcmenusapi.parser.ElementParser;
import ru.armagidon.mcmenusapi.parser.ParsingException;
import ru.armagidon.mcmenusapi.parser.tags.*;
import ru.armagidon.mcmenusapi.style.ElementStyle;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MenuParser
{

    private static final Map<Class<? extends Annotation>, ElementParser<? extends Annotation, ?>> elementParser = new ConcurrentHashMap<>();
    private static final Map<Class<? extends Annotation>, Function<? extends Annotation, Layout>> layoutParsers = new ConcurrentHashMap<>();

    static {
        elementParser.put(LinkTag.class, new LinkParser());
        elementParser.put(ButtonTag.class, new ButtonParser());
        elementParser.put(CheckBoxTag.class, new CheckBoxParser());

        registerLayoutParser(BlockLayout.class, blockLayout -> ru.armagidon.mcmenusapi.menu.layout.BlockLayout.create(blockLayout.width(), blockLayout.height()));
    }

    private static <A extends Annotation> void registerLayoutParser(Class<A> clazz,
                                                                    Function<A, Layout> factoryFunction) {
        layoutParsers.put(clazz, factoryFunction);
    }

    private static <A extends Annotation> Function<A, Layout> getLayoutFactory(Class<A> clazz) {
        return (Function<A, Layout>) layoutParsers.get(clazz);
    }


    public static ParseResult convert(Menu owner, String id, Object dataModel) {
        //Basic setup for menu
        MenuPanel defaultPanel = owner.createPanelAndAdd(id);
        owner.setModelFor(defaultPanel, dataModel);

        defaultPanel.getStyleSheet().getFrameStyle().attributes().forEach(attribute -> {
            attribute.setRaw(StyleParsingContext.createContext(defaultPanel, null, dataModel.getClass()::getAnnotation));
            attribute.reset();
        });

        Method[] methods = dataModel.getClass().getDeclaredMethods();
        Field[] fields = dataModel.getClass().getDeclaredFields();

        Supplier<Stream<AccessibleObject>> memberStream = () -> Stream.concat(Arrays.stream(methods),
                Arrays.stream(fields));

        elementParser.values().forEach(parser -> memberStream.get()
                .forEach(member -> acceptMember(member, parser, owner, defaultPanel)));

        SearchCriteria.builder()
                .type(Runnable.class)
                .annotated(RefreshFunction.class)
                .build().search(fields)
                .map(Field.class::cast)
                .findFirst().ifPresent(Unchecked.consumer(f ->
                        Reflection.setFDataSafe(f, dataModel, (Runnable) () ->
                                defaultPanel.refresh(false))));

        return new ParseResult(defaultPanel, dataModel);
    }

    private static void parseAbsoluteLayout(AccessibleObject m, MenuElement element, Map<MenuElement, Position> positionMap) {
        if (m.isAnnotationPresent(AbsolutePosition.class)) {
            if (elementParser.keySet().stream().anyMatch(m::isAnnotationPresent)) {
                AbsolutePosition p = m.getAnnotation(AbsolutePosition.class);
                positionMap.put(element, new Position(p.x(), p.y()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private static <A extends Annotation, I> void acceptMember(AccessibleObject member, ElementParser<A, I> parser, Menu owner, MenuPanel panel) {
        if (!member.isAnnotationPresent(parser.getAnnotationClass())) return;
        var parseContextBuilder = ElementParsingContext.<A, I>builder()
                .annotationData(member.getAnnotation(parser.getAnnotationClass())).owner(owner).parent(panel);
        if (member instanceof Method method) {
            if (!parser.mayBeAttachedTo().equals(ElementType.METHOD)) {
                throw new ParsingException("You cannot apply tag " + parser.getAnnotationClass().getTypeName() + " to method class member");
            } else {
                if (method.getParameterCount() != 1 && method.getParameterTypes()[0].equals(Player.class)) {
                    throw new ParsingException("Wrong argument count. Expected 1 of class Player");
                }
                parseContextBuilder.methodInvoker(Unchecked.consumer(params -> {
                    method.setAccessible(true);
                    method.invoke(owner.getModelFor(panel), params);
                }));
            }
        } else if (member instanceof Field field) {
            if (!parser.mayBeAttachedTo().equals(ElementType.FIELD)) return;

            Object data = Reflection.getFDataSafe(field, owner.getModelFor(panel));
            if (data == null) return;

            if (data instanceof List || data.getClass().isArray()) {
                DivisionBlock div = new DivisionBlock(UUID.randomUUID().toString());

                Stream<I> objectStream;
                Function<Integer, I> getter;
                BiConsumer<Integer, I> setter;
                if (data instanceof List) {
                    List<I> list = (List<I>) data;
                    objectStream = list.stream();
                    getter = list::get;
                    setter = list::set;
                } else {
                    getter = (index) -> (I) Array.get(data, index);
                    setter = (index, newValue) -> Array.set(data, index, newValue);
                    objectStream = IntStream.range(0, Array.getLength(data)).mapToObj(getter::apply);
                }

                objectStream.forEach(StreamUtils.consumerWithCounter(Unchecked.biConsumer((counter, element) -> {
                    parser.syntaxCheck((Class<I>) element.getClass(), field.getAnnotation(parser.getAnnotationClass()));

                    layoutParsers.keySet().stream().filter(field::isAnnotationPresent).findFirst().ifPresent(clazz -> {
                        Function<Annotation, Layout> factory = getLayoutFactory((Class<Annotation>) clazz);
                        var layout = factory.apply(field.getAnnotation(clazz));
                        div.setLayout(layout);
                    });

                    var context = ElementParsingContext.<A, I>builder()
                            .parent(panel)
                            .owner(owner)
                            .annotationData(member.getAnnotation(parser.getAnnotationClass()))
                            .dataGetter(Unchecked.supplier(() -> getter.apply(counter)))
                            .dataSetter(Unchecked.consumer((o) -> setter.accept(counter, o)))
                            .build();
                    MenuElement parsedElement = parser.parse(context);
                    ElementStyle style = parser.parseStyle(StyleParsingContext.createContext(context.getParent(), context.getDataGetter(), field::getAnnotation));
                    div.addElement(parsedElement);
                    panel.getStyleSheet().setStyle(parsedElement.getId(), style);
                })));

                panel.addElement(div);
                return;
            }

            parser.syntaxCheck((Class<I>) field.getType(), field.getAnnotation(parser.getAnnotationClass()));

            parseContextBuilder
                    .dataGetter(Unchecked.supplier(() -> (I) Reflection.getFDataSafe(field, owner.getModelFor(panel))))
                    .dataSetter(Unchecked.consumer((o) -> Reflection.setFDataSafe(field, owner.getModelFor(panel), o)));
        }

        var context = parseContextBuilder.build();
        MenuElement element = parser.parse(context);
        ElementStyle style = parser.parseStyle(StyleParsingContext.createContext(context.getParent(), context.getDataGetter(), member::getAnnotation));
        context.getParent().addElement(element);
        context.getParent().getStyleSheet().setStyle(element.getId(), style);
    }

    public record ParseResult(MenuPanel result, Object proxiedDataModel) {}
}
