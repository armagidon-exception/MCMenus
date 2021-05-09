package ru.armagidon.mcmenusapi.elements;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class Icon extends MenuElement
{


    public Icon(String id, ItemStack stack) {
        super(id, stack);
    }

    @Override
    public void handleClickEvent(InventoryClickEvent event) {}
}
