package ru.armagidon.mcmenusapi.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class MenuHandler implements Listener
{

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof MenuDisplay) {
            MenuDisplay display = (MenuDisplay) event.getInventory().getHolder();
            display.handleClick(event);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof MenuDisplay) {
            event.setResult(Event.Result.DENY);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof MenuDisplay) {
            MenuDisplay display = (MenuDisplay) event.getInventory().getHolder();
            display.getContext().handleClosure((Player) event.getPlayer());
        }
    }
}
