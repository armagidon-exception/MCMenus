package ru.armagidon.mcmenusapi.parser;

import ru.armagidon.mcmenusapi.elements.Button;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.misc.jool.Unchecked;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;

public class ButtonParser implements ElementParser<Method>
{

    @Override
    public MenuElement parse(ElementParsingContext<Method> input) {
        return new Button(input.getInput().getName(), Unchecked.runnable(() ->
                input.getInput().invoke(input.getDataModel(), input.getAdditionalData())));
    }

    @Override
    public ElementType supportedType() {
        return ElementType.METHOD;
    }
}
