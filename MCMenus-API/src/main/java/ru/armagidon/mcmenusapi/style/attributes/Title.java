package ru.armagidon.mcmenusapi.style.attributes;

import lombok.ToString;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.menu.MenuCanvas;
import ru.armagidon.mcmenusapi.menu.elements.RenderedElement;
import ru.armagidon.mcmenusapi.parser.tags.TitleAttribute;
import ru.armagidon.mcmenusapi.style.AttributePresenter;

import java.util.function.Function;

import static ru.armagidon.mcmenusapi.style.AttributeParser.createParser;
import static ru.armagidon.mcmenusapi.style.AttributePresenter.colorize;

@ToString
public class Title extends Attribute.ParsedAttribute<TitleAttribute, String> {

    private static final Function<TitleAttribute, String> TITlE_PARSER = (input) -> {
            if (input.isPath())
                return MCMenusAPI.getTitleRegistry().getByPath(input.title());
            else
                return input.title();
    };

    private static final AttributePresenter<Attribute<String>> PRESENTER = (attribute, usePreprocessor, input) -> {
        String title = colorize(usePreprocessor.apply(attribute.get()));
        if (input instanceof MenuCanvas canvas) {
            canvas.setTitle(title);
        } else if (input instanceof RenderedElement renderedElement) {
            renderedElement.setDisplayName(title);
        }
    };

    private Title(String defaultValue) {
        super(defaultValue, createParser(TitleAttribute.class, TITlE_PARSER), PRESENTER);
    }

    public static Title of(String input) {
        return new Title(input);
    }
}
