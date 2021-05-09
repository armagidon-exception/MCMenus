package ru.armagidon.mcmenusapi;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import ru.armagidon.mcmenusapi.menu.MenuHandler;

public final class MCMenusAPI extends JavaPlugin {


    public static NamespacedKey ID_KEY;
    @Getter
    public static MCMenusAPI instance;

    @Getter private static boolean isPAPILoaded = false;

    @Override
    public void onEnable() {
        instance = this;
        ID_KEY = new NamespacedKey(this, "element_id");
        getServer().getPluginManager().registerEvents(new MenuHandler(), this);
        isPAPILoaded = getServer().getPluginManager().getPlugin("PlaceHolderAPI") != null;
    }

    @Override
    public void onDisable() {

    }
}
