package ru.armagidon.mcmenusapi.style.attributes;

import lombok.ToString;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.parser.tags.TitlePath;
import ru.armagidon.mcmenusapi.style.AttributeParser;

@ToString
public class Title extends Attribute.SimpleAttribute<String> {

    private static final AttributeParser<String> TITlE_PARSER = ((target, input) -> {
        if (input.isAnnotationPresent(TitlePath.class)) {
            TitlePath path = input.getData(TitlePath.class);
            if (path.isPath()) target.setDefault(MCMenusAPI.getTitleRegistry().getByPath(path.title()));
            else target.setDefault(path.title());

        }
    });

    private Title(String defaultValue) {
        super(defaultValue, TITlE_PARSER);
    }

    public static Title of(String input) {
        return new Title(input);
    }
}
