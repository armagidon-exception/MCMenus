package ru.armagidon.mcmenustestplugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.armagidon.mcmenusapi.elements.CheckButton;
import ru.armagidon.mcmenusapi.menu.Menu;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.style.Title;
import ru.armagidon.mcmenusapi.utils.parser.o2m.MenuParser;

import java.util.Collections;

public final class MCMenusTestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("testmenu").setExecutor(this);
        getCommand("testmenu").setTabCompleter((sender, command, alias, args) -> Collections.emptyList());

        MenuParser.registerTexture(this, "barrel", new ItemStack(Material.BARREL));
        MenuParser.registerTexture(this, "barrier", new ItemStack(Material.BARRIER));
        MenuParser.registerTexture(this, "link", new ItemStack(Material.ARROW));
        MenuParser.registerTexture(this, "checkbox-off", new ItemStack(Material.RED_CONCRETE));
        MenuParser.registerTexture(this, "checkbox-on", new ItemStack(Material.LIME_CONCRETE));
        MenuParser.registerTexture(this, "icon", new ItemStack(Material.MAP));

    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Menu menu = new Menu();
        MenuPanel anotherPanel = new MenuPanel("test_panel1");
        TestObject object = new TestObject(anotherPanel);
        MenuPanel panel = MenuParser.objectToPanel(this, object);
        panel.getStyleSheet().getStyle("test_button").setTitle(Title.of("TEST BUTTON"));

        panel.getModel().getElement("test_checkbox").callback((e) -> sender.sendMessage("" + ((CheckButton) e).isChecked()));

        menu.addPanel(panel);
        menu.addPanel(anotherPanel);
        menu.open((Player) sender, "test_panel");
        return true;
    }
}
