package ru.armagidon.mcmenusapi.utils.parser.f2m;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang.Validate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.function.Function;

public class JsonFileReader
{

    private final JsonObject compound;

    @Getter
    private final String fileName;

    @SneakyThrows
    public JsonFileReader(File file) {
        JsonElement el = new JsonParser().parse(new BufferedReader(new FileReader(file)));
        if (el.isJsonObject()) {
            compound = (JsonObject) el;
        } else {
            compound = new JsonObject();
        }
        this.fileName = file.getName();
    }

    public int getInt(String key) {
        Validate.notEmpty(key);
        JsonElement el = compound.get(key);
        Validate.notNull(el);
        if (!el.isJsonPrimitive())
            throw new IllegalArgumentException("Value of key " + key + " is not represented by the primitive");
        JsonPrimitive primitive = (JsonPrimitive) el;
        if (!primitive.isNumber())
            throw new IllegalArgumentException("Value of key " + key + " is not represented by integer");
        return compound.getAsJsonPrimitive(key).getAsInt();
    }

    public String getString(String key) {
        Validate.notEmpty(key);
        JsonElement el = compound.get(key);
        Validate.notNull(el);
        if (!el.isJsonPrimitive())
            throw new IllegalArgumentException("Value of key " + key + " is not represented by the primitive");
        JsonPrimitive primitive = (JsonPrimitive) el;
        if (!primitive.isNumber())
            throw new IllegalArgumentException("Value of key " + key + " is not represented by string");
        return compound.getAsJsonPrimitive(key).getAsString();
    }

    public <T> T getAs(String key, Function<JsonElement, T> mapperFunction) {
        Validate.notEmpty(key);
        Validate.notNull(mapperFunction);
        JsonElement el = compound.get(key);
        Validate.notNull(el);
        return mapperFunction.apply(el);
    }
}
