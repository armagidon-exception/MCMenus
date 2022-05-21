package ru.armagidon.mcmenusapi.style.attributes;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.armagidon.mcmenusapi.data.StyleParsingContext;
import ru.armagidon.mcmenusapi.style.AttributeParser;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Attribute<T>
{
    T get();
    T getDefault();
    void setRaw(StyleParsingContext<?> raw);
    void set(T newValue);
    void setDefault(T newDefault);
    void setUpdateFunction(Consumer<Attribute<T>> updateFunction);
    default void reset() {
        set(getDefault());
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    abstract class SimpleAttribute<T> implements Attribute<T> {

        volatile T defaultValue;
        volatile T value;
        final AttributeParser<T> parser;
        volatile Consumer<Attribute<T>> updateFunction = (a) -> {};

        protected SimpleAttribute(T defaultValue, AttributeParser<T> parser) {
            this.defaultValue = defaultValue;
            this.value = defaultValue;
            this.parser = parser;
        }

        @Override
        public void setUpdateFunction(Consumer<Attribute<T>> updateFunction) {
            System.out.println("SET");
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
            parser.parse(this, raw);
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    abstract class FunctionAttribute<T, U> extends SimpleAttribute<U> {

        T value;
        Function<T, U> getter;
        BiConsumer<T, U> setter;

        protected FunctionAttribute(U defaultValue, T value, Function<T, U> getter, BiConsumer<T, U> setter, AttributeParser<U> parser) {
            super(defaultValue, parser);
            this.value = value;
            this.getter = getter;
            this.setter = setter;
        }

        @Override
        public U get() {
            return getter.apply(value);
        }

        @Override
        public void set(U newValue) {
            setter.accept(value, newValue);
        }
    }
}
