package ru.armagidon.mcmenusapi.parser.parsers;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Singular;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.data.ElementParsingContext;
import ru.armagidon.mcmenusapi.data.StyleParsingContext;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.menu.Menu;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.misc.Reflection;
import ru.armagidon.mcmenusapi.misc.jool.StreamUtils;
import ru.armagidon.mcmenusapi.misc.jool.Unchecked;
import ru.armagidon.mcmenusapi.parser.ElementParser;
import ru.armagidon.mcmenusapi.parser.RefreshInvocationHandler;
import ru.armagidon.mcmenusapi.parser.tags.ButtonTag;
import ru.armagidon.mcmenusapi.parser.tags.CheckBoxTag;
import ru.armagidon.mcmenusapi.parser.tags.LinkTag;
import ru.armagidon.mcmenusapi.parser.tags.RefreshFunction;
import ru.armagidon.mcmenusapi.style.ElementStyle;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class MenuParser
{

    private static final Map<Class<? extends Annotation>, ElementParser<? extends Annotation, ?>> parsers = new ConcurrentHashMap<>();

    static {
        parsers.put(LinkTag.class, new LinkParser());
        parsers.put(ButtonTag.class, new ButtonParser());
        parsers.put(CheckBoxTag.class, new CheckBoxParser());
    }

    @SuppressWarnings("unchecked")
    public static ParseResult convert(Menu owner, String id, Object dataModel) {
        //Basic setup for menu
        MenuPanel defaultPanel = owner.createPanelAndAdd(id);

        defaultPanel.getStyleSheet().getFrameStyle().attributes().forEach(attribute -> {
            attribute.setRaw(StyleParsingContext.createContext(defaultPanel, null, dataModel.getClass()::getAnnotation));
            attribute.reset();
        });

        //Parse tags
        //Methods
        Method[] methods = dataModel.getClass().getDeclaredMethods();
        Field[] fields = dataModel.getClass().getDeclaredFields();

        //createContext(m, dataModel, owner, defaultPanel)

        parsers.entrySet().stream()
                .filter(e -> e.getValue().mayBeAttachedTo().equals(ElementType.METHOD))
                .forEach((entry) -> SearchCriteria.builder()
                .parameterCount(1)
                .parameterType(Player.class)
                .annotated(entry.getKey())
                .build().search(methods)
                .map(m -> (Method) m)
                .forEach(m -> parseAndAddElement(ElementParsingContext.builder()
                        .annotationData(m.getAnnotation(entry.getKey()))
                        .methodInvoker(Unchecked.consumer(params -> {
                            m.setAccessible(true);
                            m.invoke(dataModel, params);
                        }))
                        .owner(owner)
                        .parent(defaultPanel)
                        .build(), (ElementParser<Annotation, Object>) entry.getValue(), m)));

        //Filter field based elements with primitive types(complex types will be used to create links)
        parsers.entrySet().stream()
                .filter(StreamUtils.inversePredicate(e -> LinkTag.class.equals(e.getKey())))
                .filter(e -> e.getValue().mayBeAttachedTo().equals(ElementType.FIELD))
                .forEach(entry -> SearchCriteria.builder()
                        .annotated(entry.getKey())
                        .types(Arrays.asList(entry.getValue().supportedTypes()))
                        .build().search(fields).map(Field.class::cast)
                        .forEach(f -> parseAndAddElement(ElementParsingContext.builder()
                                .parent(defaultPanel)
                                .owner(owner)
                                .annotationData(f.getAnnotation(entry.getKey()))
                                .dataGetter(Unchecked.supplier(() -> Reflection.getFDataSafe(f, dataModel)))
                                .dataSetter(Unchecked.consumer((o) -> Reflection.setFDataSafe(f, dataModel, o)))
                                .build(), (ElementParser<Annotation, Object>) entry.getValue(), f)));

        ElementParser<? extends Annotation, ?> linkParser = parsers.get(LinkTag.class);

        SearchCriteria.builder().
                annotated(LinkTag.class).primitive(false).
                types(Arrays.asList(linkParser.supportedTypes())).
                build().search(fields).map(Field.class::cast).
                forEach(f -> parseAndAddElement(ElementParsingContext.builder()
                    .parent(defaultPanel)
                    .owner(owner)
                    .annotationData(f.getAnnotation(LinkTag.class))
                    .dataGetter(() -> Reflection.getFDataSafe(f, dataModel))
                    .dataSetter(Unchecked.consumer((o) -> Reflection.setFDataSafe(f, dataModel, o)))
                    .build(), (ElementParser<Annotation, Object>) linkParser, f));



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

        owner.setModelFor(defaultPanel, dataModel);


        ClassLoader loader;
        if (dataModel.getClass().getInterfaces().length == 0) {
            loader = dataModel.getClass().getClassLoader();;
        } else {
            loader = dataModel.getClass().getInterfaces()[0].getClassLoader();
        }

        return new ParseResult(defaultPanel, Proxy.newProxyInstance(loader,
                dataModel.getClass().getInterfaces(),
                new RefreshInvocationHandler(dataModel, defaultPanel)));
    }


    private static <A extends Annotation, I> void parseAndAddElement(ElementParsingContext<A, I> context, ElementParser<A, I> parser, AnnotatedElement object) {
        MenuElement element = parser.parse(context);
        ElementStyle style = parser.parseStyle(StyleParsingContext.createContext(context.getParent(), context.getDataGetter(), object::getAnnotation));
        context.getParent().addElement(element, style);
    }

    public record ParseResult(MenuPanel result, Object proxiedDataModel) {

    }

    @Builder
    @SuppressWarnings("unchecked")
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static final class SearchCriteria<T extends AccessibleObject> {
        @Singular("annotated") Collection<Class<? extends Annotation>> annotated;
        @Builder.Default boolean annotatedExclude = false;
        @Builder.Default boolean primitive = true;
        @Singular Collection<Class<?>> types;
        @Singular Collection<Class<?>> parameterTypes;
        @Builder.Default int parameterCount = -1;

        public Stream<T> search(T[] input) {
            Stream<T> stream = Arrays.stream(input);

            if (!annotated.isEmpty()) {
                stream = stream.filter(obj -> {
                    if (annotatedExclude) {
                        return annotated.stream().noneMatch(obj::isAnnotationPresent);
                    } else {
                        return annotated.stream().anyMatch(obj::isAnnotationPresent);
                    }
                });
            }

            if (input instanceof Method[]) {
                if (parameterCount > -1) {
                    stream = stream.filter(m -> ((Method) m).getParameterCount() == parameterCount);
                }
                if (!parameterTypes.isEmpty()) {
                    stream = stream.map(m -> (Method) m)
                            .filter(m -> parameterTypes.stream()
                                    .allMatch(StreamUtils.predicateWithCounter((counter, clazz) ->
                                            m.getParameterTypes()[counter].equals(clazz))))
                            .map(m -> (T) m);
                }
                if (primitive) {
                    stream = stream.map(m -> (Method) m)
                            .filter(m -> m.getReturnType().isPrimitive() || m.getReturnType().equals(String.class))
                            .map(m -> (T) m);
                } else {
                    stream = stream.map(m -> (Method) m)
                            .filter(StreamUtils.inversePredicate(m -> m.getReturnType().isPrimitive() || m.getReturnType().equals(String.class)))
                            .map(m -> (T) m);
                }
                if (!types.isEmpty()) {
                    stream = stream.map(m -> (Method) m)
                            .filter(m -> types.stream().anyMatch(type -> type.equals(m.getReturnType())))
                            .map(m -> (T) m);
                }
            } else if (input instanceof Field[]) {
                    if (primitive) {
                        stream = stream.map(m -> (Field) m)
                                .filter(m -> m.getType().isPrimitive() || m.getType().equals(String.class))
                                .map(m -> (T) m);
                    } else {
                        stream = stream.map(m -> (Field) m)
                                .filter(StreamUtils.inversePredicate(m -> m.getType().isPrimitive() || m.getType().equals(String.class)))
                                .map(m -> (T) m);
                    }
                if (!types.isEmpty()) {
                    stream = stream.map(f -> (Field) f)
                            .filter(f -> types.stream().anyMatch(type -> type.equals(f.getType())))
                            .map(f -> (T) f);
                }
            }

            return stream;

        }

    }


}
