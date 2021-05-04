package ru.armagidon.mcmenusapi;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.armagidon.mcmenusapi.style.Style;

import java.util.function.Consumer;

public class MenuElement<T>
{
    private final ItemStack stack;
    private final Consumer<T> action;

    public MenuElement(ItemStack stack, Consumer<T> action) {
        this.stack = stack;
        this.action = action;
    }

    public void apply(T data) {
        action.accept(data);
    }

    public void applyStyle(Style style) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(style.getTitle().getTitle());
        meta.setLore(style.getLore().getLore());
    }
}
