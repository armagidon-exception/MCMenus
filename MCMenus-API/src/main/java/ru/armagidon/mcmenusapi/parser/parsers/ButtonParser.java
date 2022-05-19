package ru.armagidon.mcmenusapi.parser.parsers;

import ru.armagidon.mcmenusapi.elements.Button;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.misc.jool.Unchecked;
import ru.armagidon.mcmenusapi.parser.ElementParser;
import ru.armagidon.mcmenusapi.parser.ElementParsingContext;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;

public class ButtonParser implements ElementParser<Method>
{

    @Override
    public MenuElement parse(ElementParsingContext<Method> input) {
        return new Button(input.getInput().getName(), Unchecked.consumer((b) ->
                input.getInput().invoke(input.getDataModel(), input.getAdditionalData())));
    }

    @Override
    public ElementType mayBeAttachedTo() {
        return ElementType.METHOD;
    }
}
