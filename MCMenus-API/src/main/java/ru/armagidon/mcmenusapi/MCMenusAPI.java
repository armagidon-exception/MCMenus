package ru.armagidon.mcmenusapi;

import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.ItemStack;
import ru.armagidon.mcmenusapi.data.Registry;

public class MCMenusAPI
{

    public static Registry<ItemStack> getItemTextureRegistry() {
        return RegistryHolder.ITEM_TEXTURE_REGISTRY;
    }

    public static Registry<String> getItemLoreRegistry() {
        return RegistryHolder.ITEM_LORE_REGISTRY;
    }

    public static Registry<String> getTitleRegistry() {
        return RegistryHolder.TITLE_REGISTRY;
    }

    @FieldDefaults(makeFinal = true)
    private static final class RegistryHolder {
        static Registry<String> TITLE_REGISTRY = new Registry<>();
        static Registry<String> ITEM_LORE_REGISTRY = new Registry<>();
        static Registry<ItemStack> ITEM_TEXTURE_REGISTRY = new Registry<>();
    }
}
