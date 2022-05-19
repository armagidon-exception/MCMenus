package ru.armagidon.mcmenusapi.elements;

import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class MenuElement
{

    @Getter private final String id;
    @Getter private ItemStack item;
    @Getter private volatile int slot;

    public MenuElement(String id, ItemStack item) {
        Validate.notEmpty(id);
        this.item = item;
        this.id = id;
    }

    public MenuElement(String id) {
        this(id, null);
    }

    public synchronized void setItem(ItemStack item) {
        this.item = item;
    }

    public abstract void handleClickEvent(Object context, Player clicker);

    public synchronized void setSlot(int slot) {
        this.slot = slot;
    }
}
