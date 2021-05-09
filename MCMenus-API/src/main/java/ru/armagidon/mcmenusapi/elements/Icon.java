package ru.armagidon.mcmenusapi.elements;

import org.bukkit.inventory.ItemStack;
import ru.armagidon.mcmenusapi.menu.MenuDisplay;

public class Icon extends MenuElement
{

    public Icon(String id, ItemStack stack) {
        super(id, stack);
    }

    @Override
    public void handleClickEvent(MenuDisplay event) {}

}
