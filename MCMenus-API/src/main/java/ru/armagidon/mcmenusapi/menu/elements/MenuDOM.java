package ru.armagidon.mcmenusapi.menu.elements;

import lombok.ToString;
import ru.armagidon.mcmenusapi.menu.layout.Position;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ToString
public class MenuDOM
{
    private final Map<String, MenuElement> elements = new LinkedHashMap<>();

    /**
     * Adds given menu element to current menu model
     * @param element - element that will be added to {@code MenuDOM}
     * @return Whether element was added. True if element was added, false if element with given id already exists.
     * */

    public boolean addElement(MenuElement element) {
        if (!elements.containsKey(element.getId())) {
            elements.put(element.getId(), element);
            return true;
        }
        return false;
    }

    /**
     * Returns MenuElement with give id.
     * @return element with id {@param id}. If such item doesn't exists, it'll return null.
     * */
    public MenuElement getElement(String id) {
        return elements.get(id);
    }

    public Set<Map.Entry<String, MenuElement>> entrySet() {
        return elements.entrySet();
    }
}
