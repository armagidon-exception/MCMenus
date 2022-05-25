package ru.armagidon.mcmenusapi.style.attributes;

import lombok.ToString;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.menu.elements.RenderedElement;
import ru.armagidon.mcmenusapi.parser.tags.LoreAttribute;
import ru.armagidon.mcmenusapi.style.AttributePresenter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static ru.armagidon.mcmenusapi.style.AttributeParser.createParser;

@ToString
public class Lore extends Attribute.ParsedAttribute<LoreAttribute, List<String>> {

    private static final Function<LoreAttribute, List<String>> PARSER = (input) -> {
        if (input.isPath()) {
            return MCMenusAPI.getItemLoreRegistry().getByPath(input.lore()[0]);
        } else {
            return Arrays.asList(input.lore());
        }
    };

    private Lore(List<String> lore) {
        super(lore, createParser(LoreAttribute.class, PARSER), (attribute, usePreprocessor, input) -> {
            if (input instanceof RenderedElement renderedElement) {
                renderedElement.setLore(attribute.get().stream()
                        .map(usePreprocessor)
                        .map(AttributePresenter::colorize)
                        .toList());
            }
        });
    }

    public static Lore of(String... lore) {
        return new Lore(Arrays.asList(lore));
    }
}
