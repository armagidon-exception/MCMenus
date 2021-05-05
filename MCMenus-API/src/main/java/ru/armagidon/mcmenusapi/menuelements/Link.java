package ru.armagidon.mcmenusapi.menuelements;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.armagidon.mcmenusapi.MenuPanel;
import ru.armagidon.mcmenusapi.menu.MenuDisplay;

public class Link extends MenuElement
{

    private final String location;

    public Link(String id, ItemStack stack, String location) {
        super(id, stack);
        this.location = location;
    }

    @Override
    public void onClick(InventoryClickEvent data) {
        MenuDisplay display = (MenuDisplay) data.getInventory().getHolder();
        MenuPanel panel = display.getContext().getPanel(location);
        display.setCurrentPanel(panel);
    }
}
