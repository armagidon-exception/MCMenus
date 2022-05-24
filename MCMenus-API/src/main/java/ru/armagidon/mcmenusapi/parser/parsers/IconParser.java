package ru.armagidon.mcmenusapi.parser.parsers;

import ru.armagidon.mcmenusapi.data.ElementParsingContext;
import ru.armagidon.mcmenusapi.menu.elements.Icon;
import ru.armagidon.mcmenusapi.menu.elements.MenuElement;
import ru.armagidon.mcmenusapi.parser.ElementParser;
import ru.armagidon.mcmenusapi.parser.tags.IconTag;

import java.lang.annotation.ElementType;
import java.util.UUID;

public class IconParser implements ElementParser<IconTag, Object> {

    @Override
    public MenuElement parse(ElementParsingContext<IconTag, Object> input) {
        return new Icon(UUID.randomUUID().toString());
    }

    @Override
    public ElementType mayBeAttachedTo() {
        return ElementType.FIELD;
    }

    @Override
    public Class<IconTag> getAnnotationClass() {
        return IconTag.class;
    }
}
