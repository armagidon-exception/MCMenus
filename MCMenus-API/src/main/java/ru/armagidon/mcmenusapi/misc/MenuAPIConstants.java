package ru.armagidon.mcmenusapi.misc;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import ru.armagidon.mcmenusapi.MCMenusAPILoader;
import ru.armagidon.mcmenusapi.style.PlaceholderProcessingContext;

import java.util.function.BiFunction;


/**None of these constants should ever be used in static context*/
public class MenuAPIConstants
{

    public static NamespacedKey uiElementInventoryTag() {
        return MenuAPIConstantsHolder.UI_ELEMENT_INVENTORY_KEY;
    }

    public static BiFunction<PlaceholderProcessingContext, String, String> emptyPlaceholderProcessor() {
        return MenuAPIConstantsHolder.emptyPlaceholderProcessor;
    }

    private static class MenuAPIConstantsHolder {
        private static final NamespacedKey UI_ELEMENT_INVENTORY_KEY = new NamespacedKey(JavaPlugin.getPlugin(MCMenusAPILoader.class), "element_tag");
        private static final BiFunction<PlaceholderProcessingContext, String, String> emptyPlaceholderProcessor = (c, s) -> s;
    }
}
