package ru.armagidon.mcmenusapi.elements;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.menu.MenuDisplay;
import ru.armagidon.mcmenusapi.style.Style;

import java.util.function.Consumer;

public abstract class MenuElement
{

    @Getter private final String id;
    @Getter @Setter private ItemStack item;

    protected Consumer<MenuElement> callback = (e) -> {};

    public MenuElement(String id, ItemStack stack) {
        Validate.notEmpty(id);
        this.item = stack;
        this.id = id;
    }

    public abstract void handleClickEvent(MenuDisplay event);

    public void applyStyle(Style style) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(MCMenusAPI.ID_KEY, PersistentDataType.STRING, id);
        meta.setDisplayName(style.getTitle().getTitle());
        meta.setLore(style.getLore().getLore());
        item.setItemMeta(meta);
    }

    public void render(Style contextStyle, Inventory frame) {
        applyStyle(contextStyle);
        frame.setItem(contextStyle.getSlot(), item);
    }

    public void callback(Consumer<MenuElement> callback) {
        Validate.notNull(callback);
        this.callback = callback;
    }
}
