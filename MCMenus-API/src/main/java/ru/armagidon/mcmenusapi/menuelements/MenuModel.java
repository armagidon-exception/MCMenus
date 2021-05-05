package ru.armagidon.mcmenusapi.menuelements;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ToString
public class MenuModel
{
    private final Map<String, MenuElement> elements = new HashMap<>();

    /**
     * Adds given menu element to current menu model
     * @param element - element that will be added to {@code MenuModel}
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
