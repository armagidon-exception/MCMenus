package ru.armagidon.mcmenusapi.parser;

import ru.armagidon.mcmenusapi.data.StyleParsingContext;
import ru.armagidon.mcmenusapi.data.ElementParsingContext;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.style.ElementStyle;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;

public interface ElementParser<A extends Annotation, T> {

    MenuElement parse(ElementParsingContext<A, T> input);

    default ElementStyle parseStyle(StyleParsingContext<T> input) {
        ElementStyle style = new ElementStyle();
        style.attributes().forEach(attribute -> {
            attribute.setRaw(input);
            attribute.reset();
        });
        return style;
    }

    ElementType mayBeAttachedTo();

    @SuppressWarnings("unchecked")
    default Class<? extends T>[] supportedTypes() {
        return new Class[0];
    }
}
