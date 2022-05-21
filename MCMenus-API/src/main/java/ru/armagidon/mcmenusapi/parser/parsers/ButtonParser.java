package ru.armagidon.mcmenusapi.parser.parsers;

import ru.armagidon.mcmenusapi.data.ElementParsingContext;
import ru.armagidon.mcmenusapi.elements.Button;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.parser.ElementParser;
import ru.armagidon.mcmenusapi.parser.tags.ButtonTag;

import java.lang.annotation.ElementType;
import java.util.UUID;

public class ButtonParser implements ElementParser<ButtonTag, Object> {

    @Override
    public MenuElement parse(ElementParsingContext<ButtonTag, Object> input) {
        return new Button(UUID.randomUUID().toString(),
                (b) -> input.getMethodInvoker().accept(new Object[] {input.getOwner().getViewer()} ));
    }

    @Override
    public ElementType mayBeAttachedTo() {
        return ElementType.METHOD;
    }
}
