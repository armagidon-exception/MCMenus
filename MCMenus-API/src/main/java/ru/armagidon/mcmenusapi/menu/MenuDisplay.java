package ru.armagidon.mcmenusapi.menu;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.Menu;
import ru.armagidon.mcmenusapi.MenuPanel;
import ru.armagidon.mcmenusapi.menuelements.MenuElement;

import static ru.armagidon.mcmenusapi.MCMenusAPI.ID_KEY;


public class MenuDisplay implements InventoryHolder
{

    @Getter private final Menu context;
    @Getter private final Player viewer;
    @Setter
    private Inventory display;

    private MenuPanel currentPanel;

    public MenuDisplay(Menu context, Player viewer) {
        this.context = context;
        this.viewer = viewer;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return display;
    }

    public void show(MenuPanel panel) {
        Validate.notNull(panel, "Panel cannot be null");
        this.currentPanel = panel;
        panel.render(this);
        Validate.notNull(display, "Frame cannot be null");
        viewer.openInventory(display);
    }

    public void refresh(boolean rerender) {
        if (rerender) {
            currentPanel.reRender(this);
        } else {
            viewer.updateInventory();
        }
    }

    public void setCurrentPanel(MenuPanel currentPanel) {
        this.currentPanel = currentPanel;
        currentPanel.render(this);
        viewer.openInventory(display);
    }

    public void handleClick(InventoryClickEvent event) {
        if (event.getRawSlot() > display.getSize()) return;
        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
        ItemStack stack = event.getCurrentItem();
        event.setResult(Event.Result.DENY);
        if (stack == null || stack.getType().isAir() || !stack.hasItemMeta()) {
            return;
        }
        ItemMeta meta = stack.getItemMeta();
        if (!meta.getPersistentDataContainer().getKeys().contains(ID_KEY)) {
            MCMenusAPI.getInstance().getLogger().severe("Detected unregistered menu element");
            return;
        }
        String elementId = meta.getPersistentDataContainer().get(ID_KEY, PersistentDataType.STRING);
        MenuElement element = currentPanel.getModel().getElement(elementId);
        if (element == null) {
            MCMenusAPI.getInstance().getLogger().severe("Detected null menu element");
            return;
        }
        element.onClick(event);
    }
}
