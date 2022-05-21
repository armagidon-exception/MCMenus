package ru.armagidon.mcmenusapi.style;

import ru.armagidon.mcmenusapi.data.StyleParsingContext;
import ru.armagidon.mcmenusapi.style.attributes.Attribute;

//A - annotation type, I - input type
@FunctionalInterface
public interface AttributeParser<T>
{
    void parse(Attribute<T> target, StyleParsingContext<?> input);
}
