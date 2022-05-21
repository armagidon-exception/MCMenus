package ru.armagidon.mcmenusapi.data;

import ru.armagidon.mcmenusapi.menu.MenuPanel;

import java.lang.annotation.Annotation;
import java.util.function.Function;
import java.util.function.Supplier;

public interface StyleParsingContext<T>
{
    <A extends Annotation> A getData(Class<A> annotationClass);
    Supplier<T> getDataGetter();
    MenuPanel getParent();

    default boolean isAnnotationPresent(Class<? extends Annotation> clazz) {
        return getData(clazz) != null;
    }


    static <T> StyleParsingContext<T> createContext(MenuPanel parent, Supplier<T> getter, Function<Class<? extends Annotation>, ? extends Annotation> annotationGetter) {
        return new StyleParsingContext<>() {

            @Override
            @SuppressWarnings("unchecked")
            public <A extends Annotation> A getData(Class<A> annotationClass) {
                return (A) annotationGetter.apply(annotationClass);
            }

            @Override
            public Supplier<T> getDataGetter() {
                return getter;
            }

            @Override
            public MenuPanel getParent() {
                return parent;
            }
        };
    }
}
