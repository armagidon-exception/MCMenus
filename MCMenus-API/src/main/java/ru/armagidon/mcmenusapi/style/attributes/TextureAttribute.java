package ru.armagidon.mcmenusapi.style.attributes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.parser.tags.ItemTexturePath;
import ru.armagidon.mcmenusapi.style.AttributeParser;

import java.lang.reflect.AccessibleObject;


public class TextureAttribute extends Attribute.SimpleAttribute<ItemStack> {

    private static final AttributeParser<ItemStack> PARSER = (target, input) -> {
        if (input.isAnnotationPresent(ItemTexturePath.class)) {
            ItemTexturePath texturePath = input.getData(ItemTexturePath.class);
            target.setDefault(MCMenusAPI.getItemTextureRegistry().getByPath(texturePath.path()));
        }
    };

    private TextureAttribute(ItemStack defaultValue) {
        super(defaultValue.clone(), PARSER);
    }

    public static TextureAttribute of(ItemStack texture) {
        return new TextureAttribute(texture);
    }

    public static TextureAttribute of(Material texture) {
        return of(new ItemStack(texture));
    }
}
