package ru.armagidon.mcmenustestplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.menu.Menu;

import java.util.Collections;

public final class MCMenusTestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("testmenu").setExecutor(this);
        getCommand("testmenu").setTabCompleter((sender, command, alias, args) -> Collections.emptyList());

        MCMenusAPI.getItemTextureRegistry().register("arrow", new ItemStack(Material.ARROW));
        MCMenusAPI.getItemTextureRegistry().register("barrier", new ItemStack(Material.BARRIER));
        MCMenusAPI.getItemTextureRegistry().register("checkbox", new ItemStack(Material.RED_CONCRETE));
        MCMenusAPI.getItemTextureRegistry().register("checked_texture", new ItemStack(Material.LIME_CONCRETE));

    }

    @Override
    public void onDisable() {

    }

    boolean state = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        var t2 = new TestObject2();
        TestObj object = Menu.convertAndOpenMenu(this, (Player) sender, new TestObject(t2));
        Bukkit.getScheduler().runTaskTimer(MCMenusTestPlugin.getProvidingPlugin(MCMenusTestPlugin.class), () -> {
            object.setTestCheckButton(!state);
            state = !state;
        }, 5, 5);
        return true;
    }
}
