package ru.armagidon.mcmenusapi.parser.parsers;

import lombok.SneakyThrows;
import ru.armagidon.mcmenusapi.elements.Link;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.parser.ElementParser;
import ru.armagidon.mcmenusapi.parser.ElementParsingContext;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;

public class LinkParser implements ElementParser<Field>
{

    @Override
    @SneakyThrows
    public MenuElement parse(ElementParsingContext<Field> input) {
        input.getInput().setAccessible(true);
        return new Link(input.getInput().getName(), MenuParser.convert(input.getOwner(), input.getInput().getName(), input.getInput().get(input.getDataModel())));
    }

    @Override
    public ElementType mayBeAttachedTo() {
        return ElementType.FIELD;
    }
}
