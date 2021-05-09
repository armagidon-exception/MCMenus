package ru.armagidon.mcmenusapi.utils.parser;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.elements.CheckButton;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.utils.parser.tags.CheckBoxTag;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

class CheckBoxParser implements ElementParser
{

    @Override
    public void parse(Plugin plugin, MenuPanel context, Object src) {
        Field[] fields = src.getClass().getDeclaredFields();

        Arrays.stream(fields).peek(f -> f.setAccessible(true)).filter(f -> f.isAnnotationPresent(CheckBoxTag.class))
                .filter(f -> f.getType().equals(boolean.class) || f.getType().equals(Boolean.class)).filter(f -> (f.getModifiers() & Modifier.STATIC) == 0).forEach(f -> {

            CheckBoxTag checkBoxData = f.getDeclaredAnnotation(CheckBoxTag.class);

            ItemStack onTexture = MenuParser.textureRegistry.get(MenuParser.of(plugin, checkBoxData.texture() + "-on"));
            ItemStack offTexture = MenuParser.textureRegistry.get(MenuParser.of(plugin, checkBoxData.texture() + "-off"));

            if (onTexture == null || offTexture == null) {
                MCMenusAPI.getInstance().getLogger().severe("Texture " + checkBoxData.texture() + " for link " + checkBoxData.id() + " wasn't found in the registry. Skipping.");
                return;
            }

            CheckButton checkBox = new CheckBoxDecorator(checkBoxData.id(), offTexture, onTexture, checkBoxData.checked(), f, src);
            context.addElement(checkBox);

        });
    }
}
