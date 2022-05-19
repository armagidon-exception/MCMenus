package ru.armagidon.mcmenusapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class MCMenusAPILoader extends JavaPlugin {

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    public static Logger getPluginLogger() {
        return JavaPlugin.getPlugin(MCMenusAPILoader.class).getLogger();
    }

    public static boolean isPAPILoaded() {
        return Bukkit.getServer().getPluginManager().getPlugin("PlaceHolderAPI") != null;
    }
}
