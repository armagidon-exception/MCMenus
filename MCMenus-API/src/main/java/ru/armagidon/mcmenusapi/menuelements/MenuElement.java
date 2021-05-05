package ru.armagidon.mcmenusapi.menuelements;

import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.MenuPanel;
import ru.armagidon.mcmenusapi.style.Style;

public abstract class MenuElement
{

    @Getter private final String id;
    @Getter private ItemStack item;

    public MenuElement(String id, ItemStack stack) {
        Validate.notEmpty(id);
        this.item = stack;
        this.id = id;
    }

    public abstract void onClick(InventoryClickEvent event);

    public void applyStyle(Style style) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(MCMenusAPI.ID_KEY, PersistentDataType.STRING, id);
        meta.setDisplayName(style.getTitle().getTitle());
        meta.setLore(style.getLore().getLore());
        item.setItemMeta(meta);
    }

    public void render(Style contextStyle, Inventory frame) {
        applyStyle(contextStyle);
        frame.setItem(contextStyle.getX() + contextStyle.getY() * MenuPanel.MENU_WIDTH, item);
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }
}
