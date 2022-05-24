package ru.armagidon.mcmenusapi.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuCanvas implements Renderable
{
    final MenuPanel holder;
    final ItemStack[] items;
    volatile Inventory display;
    @Getter volatile String title;
    @Getter volatile InventoryType type;
    @Getter volatile int size;

    private static final int MAX_CHEST_STORAGE = 54;


    public MenuCanvas(MenuPanel holder, String title, InventoryType type, int size) {
        this.holder = holder;
        this.title = title;
        this.type = type;
        this.size = size;
        items = new ItemStack[MAX_CHEST_STORAGE];
        updateCanvas();
    }

    public MenuCanvas(MenuPanel holder, String title, InventoryType type) {
        this(holder, title, type, type.getDefaultSize());
    }

    public MenuCanvas(MenuPanel holder, String title, int size) {
        this(holder, title, InventoryType.CHEST, size);
    }

    public MenuCanvas(MenuPanel holder) {
        this(holder, InventoryType.CHEST.getDefaultTitle(), InventoryType.CHEST.getDefaultSize());
    }

    public void setItem(int slot, ItemStack stack) {
        if (slot < 0 || slot >= size) return;
        items[slot] = stack;
        display.setItem(slot, stack);
    }

    public void setTitle(String title) {
        this.title = title;
        updateCanvas();
    }

    public void setType(InventoryType type) {
        this.type = type;
        updateCanvas();
    }

    public void setSize(int size) {
        this.size = size;
        if (type.equals(InventoryType.CHEST))
            updateCanvas();
    }

    private void updateCanvas() {
        if (type.equals(InventoryType.CHEST))
            display = Bukkit.createInventory(holder, size, title);
        else
            display = Bukkit.createInventory(holder, type, title);
        display.setContents(Arrays.copyOfRange(items, 0, size));
    }

    public Inventory getDisplay() {
        return display;
    }
}
