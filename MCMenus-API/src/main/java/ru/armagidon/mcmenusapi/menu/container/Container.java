package ru.armagidon.mcmenusapi.menu.container;

import ru.armagidon.mcmenusapi.menu.elements.MenuElement;
import ru.armagidon.mcmenusapi.menu.layout.Layout;

import java.util.Set;

public interface Container
{
    void addElement(MenuElement element);
    MenuElement getElement(String id);
    Set<MenuElement> getChildren();
    Layout getLayout();
    void setLayout(Layout layout);
}
