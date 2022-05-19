package ru.armagidon.mcmenusapi.style.attributes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class TextureAttribute implements Attribute<ItemStack>
{

    private volatile ItemStack texture;

    public TextureAttribute(ItemStack texture) {
        this.texture = texture;
    }

    public static TextureAttribute of(ItemStack texture) {
        return new TextureAttribute(texture);
    }

    public static TextureAttribute of(Material texture) {
        return of(new ItemStack(texture));
    }

    @Override
    public ItemStack get() {
        return texture;
    }

    @Override
    public synchronized void set(ItemStack newValue) {
        this.texture = newValue;
    }
}
