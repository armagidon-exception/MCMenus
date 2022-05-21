package ru.armagidon.mcmenusapi.misc;

import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class Reflection
{
    @SneakyThrows
    public static Object getFDataSafe(String name, Object holder) {
        Field field = holder.getClass().getDeclaredField(name);
        return getFDataSafe(field, holder);
    }

    @SneakyThrows
    public static void setFDataSafe(String name, Object holder, Object newValue) {
        Field field = holder.getClass().getDeclaredField(name);
        setFDataSafe(field, holder, newValue);
    }

    @SneakyThrows
    public static Object getFDataSafe(Field field, Object holder) {
        field.setAccessible(true);
        return field.get(holder);
    }

    @SneakyThrows
    public static void setFDataSafe(Field field, Object holder, Object newValue) {
        field.setAccessible(true);
        field.set(holder, newValue);
    }
}
