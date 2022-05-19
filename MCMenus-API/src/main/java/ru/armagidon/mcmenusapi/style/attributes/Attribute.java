package ru.armagidon.mcmenusapi.style.attributes;

public interface Attribute<T>
{
    T get();
    void set(T newValue);
    T getDefault();
    void setDefault(T newDefault);
    default void reset() {
        set(getDefault());
    }
}
