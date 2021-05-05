package ru.armagidon.mcmenusapi.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.armagidon.mcmenusapi.Menu;
import ru.armagidon.mcmenusapi.MenuPanel;
import ru.armagidon.mcmenusapi.menuelements.CheckButton;
import ru.armagidon.mcmenusapi.style.Title;

public class MenuHandler implements Listener
{
    private final Menu menu;


    public MenuHandler() {
        menu = new Menu();
        MenuPanel panel = new MenuPanel();
        panel.addElement(new CheckButton("test_icon", new ItemStack(Material.RED_DYE), new ItemStack(Material.GREEN_DYE), false));
        panel.getStyleSheet().setTitle("TEST TITLE");
        panel.getStyleSheet().setSize(3);
        panel.getStyleSheet().getStyle("test_icon").setX(4);
        panel.getStyleSheet().getStyle("test_icon").setTitle(Title.of("Test check button"));

        menu.addPanel("test_panel", panel);

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof MenuDisplay) {
            MenuDisplay display = (MenuDisplay) event.getInventory().getHolder();
            display.handleClick(event);
        }
    }

    @EventHandler
    public void on(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof MenuDisplay) {
            event.setResult(Event.Result.DENY);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof MenuDisplay) {
            MenuDisplay display = (MenuDisplay) event.getInventory().getHolder();
            display.getContext().closeInventory((Player) event.getPlayer());
        }
    }

    @EventHandler
    public void onChat(PlayerInteractEvent event) {
        menu.open(event.getPlayer(), "test_panel");
    }
}
