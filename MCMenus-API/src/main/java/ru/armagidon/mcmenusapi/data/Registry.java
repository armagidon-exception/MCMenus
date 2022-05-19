package ru.armagidon.mcmenusapi.data;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Registry<I>
{
    Map<String, I> registry = new ConcurrentHashMap<>();
    Supplier<I> defaultSupplier;

    public Registry(Supplier<I> defaultSupplier) {
        this.defaultSupplier = defaultSupplier;
    }

    public void register(String path, I data) {
        registry.putIfAbsent(path, data);
    }

    public I getByPath(String path) {
        return registry.getOrDefault(path, getDefaultValue());
    }

    public I getDefaultValue() {
        return defaultSupplier.get();
    }
}
