package ru.armagidon.mcmenusapi.misc;

import org.bukkit.NamespacedKey;

import java.util.function.BiFunction;


/**None of these constants should ever be used in static context*/
public class MenuAPIConstants
{

    public static NamespacedKey uiElementInventoryTag() {
        return MenuAPIConstantsHolder.UI_ELEMENT_INVENTORY_KEY;
    }

    private static class MenuAPIConstantsHolder {
        private static final NamespacedKey UI_ELEMENT_INVENTORY_KEY = new NamespacedKey("mcmenusapi", "element_tag");
    }
}
