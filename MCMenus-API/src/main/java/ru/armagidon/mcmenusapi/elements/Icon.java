package ru.armagidon.mcmenusapi.elements;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Icon extends MenuElement
{

    public Icon(String id, ItemStack item) {
        super(id, item);
    }

    @Override
    public void handleClickEvent(Object context, Player clicker) {

    }
}
