package ru.armagidon.mcmenusapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import ru.armagidon.mcmenusapi.style.MenuStyleSheet;

public class Menu implements InventoryHolder
{
    private MenuModel model;
    private MenuStyleSheet styleSheet;
    private Inventory display;

    public void render() {
        //TODO render menu
        //Phase #1 Setting size of the menu
        display = Bukkit.createInventory(this, styleSheet.getSize(), ChatColor.translateAlternateColorCodes('&', styleSheet.getTitle()));
    }

    @Override
    public @NotNull Inventory getInventory() {
        return display;
    }
}
