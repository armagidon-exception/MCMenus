package ru.armagidon.mcmenusapi.parser.parsers;

import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.data.ElementParsingContext;
import ru.armagidon.mcmenusapi.data.StyleParsingContext;
import ru.armagidon.mcmenusapi.menu.elements.MenuElement;
import ru.armagidon.mcmenusapi.menu.Menu;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.misc.Reflection;
import ru.armagidon.mcmenusapi.misc.jool.StreamUtils;
import ru.armagidon.mcmenusapi.misc.jool.Unchecked;
import ru.armagidon.mcmenusapi.parser.ElementParser;
import ru.armagidon.mcmenusapi.parser.ParsingException;
import ru.armagidon.mcmenusapi.parser.tags.ButtonTag;
import ru.armagidon.mcmenusapi.parser.tags.CheckBoxTag;
import ru.armagidon.mcmenusapi.parser.tags.LinkTag;
import ru.armagidon.mcmenusapi.parser.tags.RefreshFunction;
import ru.armagidon.mcmenusapi.style.ElementStyle;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MenuParser
{

    private static final Map<Class<? extends Annotation>, ElementParser<? extends Annotation, ?>> parsers = new ConcurrentHashMap<>();

    static {
        parsers.put(LinkTag.class, new LinkParser());
        parsers.put(ButtonTag.class, new ButtonParser());
        parsers.put(CheckBoxTag.class, new CheckBoxParser());
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

        Supplier<Stream<AccessibleObject>> memberStream = () -> Stream.concat(Arrays.stream(methods), Arrays.stream(fields));

        parsers.values().forEach(parser -> memberStream.get().forEach(member -> acceptMember(member, parser, owner, defaultPanel)));

        SearchCriteria.builder()
                .type(Runnable.class)
                .annotated(RefreshFunction.class)
                .build().search(fields)
                .map(Field.class::cast)
                .findFirst().ifPresent(Unchecked.consumer(f -> {
                    f.setAccessible(true);
                    Runnable refreshFunction = () -> defaultPanel.refresh(false);
                    f.set(dataModel, refreshFunction);
                }));

        return new ParseResult(defaultPanel, dataModel);
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
                    parseAndAddElement(ElementParsingContext.<A, I>builder()
                            .parent(panel)
                            .owner(owner)
                            .annotationData(member.getAnnotation(parser.getAnnotationClass()))
                            .dataGetter(Unchecked.supplier(() -> getter.apply(counter)))
                            .dataSetter(Unchecked.consumer((o) -> setter.accept(counter, o)))
                            .build(), parser, field);
                })));
                return;
            }

            parser.syntaxCheck((Class<I>) field.getType(), field.getAnnotation(parser.getAnnotationClass()));

            parseContextBuilder
                    .dataGetter(Unchecked.supplier(() -> (I) Reflection.getFDataSafe(field, owner.getModelFor(panel))))
                    .dataSetter(Unchecked.consumer((o) -> Reflection.setFDataSafe(field, owner.getModelFor(panel), o)));
        }

        parseAndAddElement(parseContextBuilder.build(), parser, member);
    }

    private static <A extends Annotation, I> void parseAndAddElement(ElementParsingContext<A, I> context, ElementParser<A, I> parser, AnnotatedElement object) {
        MenuElement element = parser.parse(context);
        ElementStyle style = parser.parseStyle(StyleParsingContext.createContext(context.getParent(), context.getDataGetter(), object::getAnnotation));
        context.getParent().addElement(element, style);
    }

    public record ParseResult(MenuPanel result, Object proxiedDataModel) {}
}
