package ru.armagidon.mcmenusapi.style.attributes;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.armagidon.mcmenusapi.data.StyleParsingContext;
import ru.armagidon.mcmenusapi.menu.Renderable;
import ru.armagidon.mcmenusapi.style.AttributeParser;
import ru.armagidon.mcmenusapi.style.AttributePresenter;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public interface Attribute<T>
{
    T get();

    T getDefault();

    void setRaw(StyleParsingContext<?> raw);

    void set(T newValue);

    void setDefault(T newDefault);

    void setUpdateFunction(Consumer<Attribute<T>> updateFunction);

    void display(UnaryOperator<String> usePreprocessor, Renderable input);

    default void reset() {
        set(getDefault());
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    abstract class ParsedAttribute<A extends Annotation, T> implements Attribute<T> {

        volatile T defaultValue;
        volatile T value;
        volatile Consumer<Attribute<T>> updateFunction = (a) -> {};
        final AttributeParser<A, T> parser;
        final AttributePresenter<Attribute<T>> presenter;

        protected ParsedAttribute(T defaultValue, AttributeParser<A, T> parser, AttributePresenter<Attribute<T>> presenter) {
            this.defaultValue = defaultValue;
            this.value = defaultValue;
            this.parser = parser;
            this.presenter = presenter;
        }

        @Override
        public void setUpdateFunction(Consumer<Attribute<T>> updateFunction) {
            this.updateFunction = updateFunction;
        }

        @Override
        public T get() {
            updateFunction.accept(this);
            return value;
        }

        @Override
        public synchronized void set(T newValue) {
            this.value = newValue;
        }

        @Override
        public T getDefault() {
            return defaultValue;
        }

        @Override
        public synchronized void setDefault(T newDefault) {
            this.defaultValue = newDefault;
        }

        @Override
        public void setRaw(StyleParsingContext<?> raw) {
            if (raw.isAnnotationPresent(parser.getAnnotationClass())) {
                setDefault(parser.parse(raw.getData(parser.getAnnotationClass())));
            }
        }

        @Override
        public void display(UnaryOperator<String> usePreprocessor, Renderable input) {
            presenter.present(this, usePreprocessor, input);
        }
    }
}
