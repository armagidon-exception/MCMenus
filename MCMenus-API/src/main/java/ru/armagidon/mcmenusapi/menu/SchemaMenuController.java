package ru.armagidon.mcmenusapi.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.misc.MenuAPIConstants;


/**
 * This class represents the controller of MenuDisplay and MenuPanel for the view
 * And of some state object for model
 * */
public class SchemaMenuController implements Listener
{

    private final Menu menu;

    public SchemaMenuController(Menu menu) {
        this.menu = menu;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof MenuPanel display) {
            if (event.getRawSlot() > display.getInventory().getSize()) return;
            if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
            ItemStack stack = event.getCurrentItem();
            event.setResult(Event.Result.DENY);
            if (stack == null || stack.getType().isAir() || !stack.hasItemMeta()) {
                return;
            }
            ItemMeta meta = stack.getItemMeta();
            if (!meta.getPersistentDataContainer().getKeys().contains(MenuAPIConstants.uiElementInventoryTag())) {
                return;
            }
            String elementId = meta.getPersistentDataContainer().get(MenuAPIConstants.uiElementInventoryTag(), PersistentDataType.STRING);
            MenuElement element = display.getMenuDOM().getElement(elementId);
            if (element == null) {
                return;
            }
            element.handleClickEvent(menu.getModelFor(display), (Player) event.getWhoClicked());
            display.refresh(false);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof MenuPanel) {
            event.setResult(Event.Result.DENY);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof MenuPanel menuPanel) {
            menuPanel.close();
        }
    }
}
