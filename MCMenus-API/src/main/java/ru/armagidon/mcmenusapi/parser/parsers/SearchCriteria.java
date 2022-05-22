package ru.armagidon.mcmenusapi.parser.parsers;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Singular;
import lombok.experimental.FieldDefaults;
import ru.armagidon.mcmenusapi.misc.jool.StreamUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

@Builder
@SuppressWarnings("unchecked")
@FieldDefaults(level = AccessLevel.PRIVATE)
final class SearchCriteria<T extends AccessibleObject> {
    @Singular("annotated")
    Collection<Class<? extends Annotation>> annotated;
    @Singular("excludeAnnotation") Collection<Class<? extends Annotation>> annotationExclude;
    @Singular Collection<Class<?>> types;
    @Singular Collection<Class<?>> parameterTypes;
    @Builder.Default int parameterCount = -1;

    public SearchCriteriaBuilder<T> modifer() {
        return SearchCriteria.<T>builder().parameterCount(parameterCount)
                .parameterTypes(parameterTypes)
                .annotated(annotated)
                .annotationExclude(annotationExclude)
                .types(types);
    }

    public Stream<T> search(T[] input) {
        Stream<T> stream = Arrays.stream(input);

        if (!annotated.isEmpty()) {
            stream = stream.filter(obj -> annotated.stream().anyMatch(obj::isAnnotationPresent));
        }
        if (!annotationExclude.isEmpty()) {
            stream = stream.filter(obj -> annotationExclude.stream().noneMatch(obj::isAnnotationPresent)).peek(System.out::println);
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
            if (!types.isEmpty()) {
                stream = stream.map(m -> (Method) m)
                        .filter(m -> types.stream().anyMatch(type -> type.equals(m.getReturnType())))
                        .map(m -> (T) m);
            }
        } else if (input instanceof Field[]) {
            if (!types.isEmpty()) {
                stream = stream.map(f -> (Field) f)
                        .filter(f -> types.stream().anyMatch(type -> type.equals(f.getType())))
                        .map(f -> (T) f);
            }
        }

        return stream;

    }

}
