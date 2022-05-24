package ru.armagidon.mcmenusapi.style;

import java.lang.annotation.Annotation;
import java.util.function.Function;

//A - annotation type, I - input type

public interface AttributeParser<A extends Annotation, T>
{
    T parse(A annotation);
    Class<A> getAnnotationClass();

    static <A extends Annotation, T> AttributeParser<A, T> createParser(Class<A> annotationClazz, Function<A, T> function) {
        return new AttributeParser<>() {
            @Override
            public T parse(A annotation) {
                return function.apply(annotation);
            }

            @Override
            public Class<A> getAnnotationClass() {
                return annotationClazz;
            }
        };
    }

}
