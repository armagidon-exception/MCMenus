package ru.armagidon.mcmenusapi.data;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Registry<I>
{
    Map<String, I> registry = new ConcurrentHashMap<>();

    public void register(String path, I data) {
        registry.putIfAbsent(path, data);
    }

    public I getByPath(String path) {
        return registry.get(path);
    }
}
