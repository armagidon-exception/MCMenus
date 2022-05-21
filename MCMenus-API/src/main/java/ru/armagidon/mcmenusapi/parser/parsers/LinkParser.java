package ru.armagidon.mcmenusapi.parser.parsers;

import ru.armagidon.mcmenusapi.data.ElementParsingContext;
import ru.armagidon.mcmenusapi.elements.Link;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.parser.ElementParser;
import ru.armagidon.mcmenusapi.parser.tags.LinkTag;

import java.lang.annotation.ElementType;
import java.util.UUID;

public class LinkParser implements ElementParser<LinkTag, Object> {

    @Override
    public MenuElement parse(ElementParsingContext<LinkTag, Object> input) {
        return new Link(UUID.randomUUID().toString(), MenuParser.convert(input.getOwner(), UUID.randomUUID().toString(), input.getDataGetter().get()).result());
    }

    @Override
    public ElementType mayBeAttachedTo() {
        return ElementType.FIELD;
    }
}
