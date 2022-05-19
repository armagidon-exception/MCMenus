package ru.armagidon.mcmenusapi.style.attributes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class TextureAttribute implements Attribute<ItemStack>
{

    private volatile ItemStack texture;
    private ItemStack defaultValue;

    public TextureAttribute(ItemStack texture) {
        this.texture = texture;
        this.defaultValue = texture.clone();
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

    @Override
    public ItemStack getDefault() {
        return defaultValue;
    }

    @Override
    public void setDefault(ItemStack newDefault) {
        this.defaultValue = newDefault;
    }
}
