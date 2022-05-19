package ru.armagidon.mcmenusapi.parsers;

import ru.armagidon.mcmenusapi.elements.Link;
import ru.armagidon.mcmenusapi.elements.MenuElement;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;

public class LinkParser implements ElementParser<Field>
{

    @Override
    public MenuElement parse(Field input) {
        return new Link(input.getName());
    }

    @Override
    public ElementType supportedType() {
        return ElementType.FIELD;
    }
}
