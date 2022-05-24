package ru.armagidon.mcmenusapi.menu.elements;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.armagidon.mcmenusapi.menu.Renderable;
import ru.armagidon.mcmenusapi.misc.NBTModifier;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class RenderedElement implements Renderable
{
    ItemStack item;
    ItemMeta propertyHandle;

    private RenderedElement(ItemStack item) {
        this.item = item;
        this.propertyHandle = item.getItemMeta();
    }
    
    public static RenderedElement create(Material material){
        return create(new ItemStack(material));
    }

    public static RenderedElement create(ItemStack stack){
        return new RenderedElement(stack);
    }

    public RenderedElement setDisplayName(String name){
        propertyHandle.setDisplayName(name);
        item.setItemMeta(propertyHandle);
        return this;
    }

    public RenderedElement setLore(List<String> s){
        propertyHandle.setLore(s);
        item.setItemMeta(propertyHandle);
        return this;
    }

    public RenderedElement setEnchantment(Enchantment enchantment, int level){
        propertyHandle.addEnchant(enchantment, level, true);
        item.setItemMeta(propertyHandle);
        return this;
    }

    public RenderedElement setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public RenderedElement setType(Material type) {
        item.setType(type);
        return this;
    }

    public RenderedElement setStringTag(String key, String value){
        NBTModifier.setString(item, key, value);
        return this;
    }

    public RenderedElement setItem(ItemStack stack) {
        this.item = stack;
        ItemMeta newMeta = item.getItemMeta();
        if (newMeta == null) return this;
        if (!item.hasItemMeta()){
            stack.setItemMeta(propertyHandle);
            return this;
        }
        if (propertyHandle.hasDisplayName())
            newMeta.setDisplayName(propertyHandle.getDisplayName());
        if (propertyHandle.hasLore())
            newMeta.setLore(propertyHandle.getLore());
        if (propertyHandle.hasEnchants())
            propertyHandle.getEnchants().forEach((enchantment, level) ->
                    newMeta.addEnchant(enchantment, level, true));
        if (propertyHandle.isUnbreakable())
            newMeta.setUnbreakable(true);
        if (propertyHandle.hasAttributeModifiers())
            newMeta.setAttributeModifiers(propertyHandle.getAttributeModifiers());
        newMeta.addItemFlags(propertyHandle.getItemFlags().toArray(ItemFlag[]::new));
        this.propertyHandle = newMeta;
        return this;
    }

    public RenderedElement setEnchanted(boolean enchanted){
        if(enchanted){
            propertyHandle.addEnchant(Enchantment.DURABILITY, 1, true);
            propertyHandle.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            propertyHandle.removeEnchant(Enchantment.DURABILITY);
        }
        item.setItemMeta(propertyHandle);
        return this;
    }

    public ItemStack asItemStack() {
        return item;
    }

    public RenderedElement addFlags(ItemFlag... flags){
        propertyHandle.addItemFlags(flags);
        item.setItemMeta(propertyHandle);
        return this;
    }
}
