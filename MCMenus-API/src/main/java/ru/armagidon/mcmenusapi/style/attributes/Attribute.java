package ru.armagidon.mcmenusapi.style.attributes;

public interface Attribute<T>
{
    T get();
    void set(T newValue);
}
