package ru.armagidon.mcmenustestplugin;

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

    TestObject obj = new TestObject(new TestObject2());

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Menu.convertAndOpenMenu(this, (Player) sender, obj);
        return true;
    }
}
