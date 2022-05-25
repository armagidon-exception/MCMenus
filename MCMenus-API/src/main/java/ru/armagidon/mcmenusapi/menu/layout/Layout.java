package ru.armagidon.mcmenusapi.menu.layout;

import ru.armagidon.mcmenusapi.menu.elements.MenuElement;

import java.util.Map;
import java.util.Set;

public interface Layout {

    Map<MenuElement, Position> apply(Set<MenuElement> elements, Position offset, Dimension frameSize);
    default Map<MenuElement, Position> apply(Set<MenuElement> elements) {
        return apply(elements, new Position(), new Dimension());
    }

    Dimension getSize();
}
