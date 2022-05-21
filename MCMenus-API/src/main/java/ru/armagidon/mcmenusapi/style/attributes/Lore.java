package ru.armagidon.mcmenusapi.style.attributes;

import lombok.ToString;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.parser.tags.LorePath;
import ru.armagidon.mcmenusapi.style.AttributeParser;

import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.List;

@ToString
public class Lore extends Attribute.SimpleAttribute<List<String>> {

    private static final AttributeParser<List<String>> PARSER = (target, input) -> {
        if (input.isAnnotationPresent(LorePath.class)) {
            LorePath lorePath = input.getData(LorePath.class);
            if (lorePath.isPath()) {
                target.setDefault(MCMenusAPI.getItemLoreRegistry().getByPath(lorePath.lore()[0]));
            } else {
                target.setDefault(Arrays.asList(lorePath.lore()));
            }
        }
    };

    private Lore(List<String> lore) {
        super(lore, PARSER);
    }

    public static Lore of(String... lore) {
        return new Lore(Arrays.asList(lore));
    }
}
