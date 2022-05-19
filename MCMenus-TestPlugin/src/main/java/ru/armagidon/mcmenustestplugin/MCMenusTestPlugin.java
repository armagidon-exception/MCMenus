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

        MCMenusAPI.getItemTextureRegistry().register("barrel", new ItemStack(Material.BARREL));
        MCMenusAPI.getItemTextureRegistry().register("barrier", new ItemStack(Material.BARRIER));
        MCMenusAPI.getItemTextureRegistry().register("link", new ItemStack(Material.ARROW));
        MCMenusAPI.getItemTextureRegistry().register("checkbox-off", new ItemStack(Material.RED_CONCRETE));
        MCMenusAPI.getItemTextureRegistry().register("checkbox-on", new ItemStack(Material.LIME_CONCRETE));
        MCMenusAPI.getItemTextureRegistry().register("icon", new ItemStack(Material.MAP));

    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        TestObject object = new TestObject(new TestObject2());
        Menu.convertAndOpenMenu(this, (Player) sender, object);
        return true;
    }
}
