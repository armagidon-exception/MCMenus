package ru.armagidon.mcmenusapi.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import ru.armagidon.mcmenusapi.elements.MenuDOM;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.misc.MenuAPIConstants;
import ru.armagidon.mcmenusapi.style.ElementStyle;
import ru.armagidon.mcmenusapi.style.MenuStyleSheet;
import ru.armagidon.mcmenusapi.style.PlaceholderProcessingContext;
import ru.armagidon.mcmenusapi.style.attributes.Lore;
import ru.armagidon.mcmenusapi.style.attributes.TextureAttribute;
import ru.armagidon.mcmenusapi.style.attributes.Title;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@ToString
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuPanel implements InventoryHolder
{

    MenuDOM menuDOM;
    MenuStyleSheet styleSheet;
    @Getter String id;
    Player viewer;
    @NonFinal volatile Inventory display;
    @NonFinal volatile boolean shown;

    public MenuPanel(String id, Player viewer) {
        Validate.notEmpty(id);
        this.viewer = viewer;
        this.id = id;
        this.styleSheet = new MenuStyleSheet();
        this.menuDOM = new MenuDOM();
    }

    public synchronized void show() {
        shown = true;
        render();
        viewer.openInventory(display);
    }

    void close() {
        shown = false;
    }

    public boolean isShown() {
        return shown;
    }

    public void render() {
        refresh(true);
    }

    public MenuDOM getMenuDOM() {
        return menuDOM;
    }

    public MenuStyleSheet getStyleSheet() {
        return styleSheet;
    }

    private int getFreeSlot() {
        return IntStream.
                rangeClosed(1, getStyleSheet().getHeadStyle().getMenuSize() * getStyleSheet().getHeadStyle().getMenuLookType().getElementsMinimum())
                .filter(i -> menuDOM.entrySet().stream().map(Map.Entry::getValue).map(MenuElement::getSlot).noneMatch(s -> s == i))
                .findFirst().getAsInt();
    }

    //Refreshing
    private synchronized void setCanvas(String title, int size, InventoryType type) {
        title = styleSheet.getHeadStyle().getPlaceHolderProcessor().apply(new PlaceholderProcessingContext(viewer), color(title));
        if (type.equals(InventoryType.CHEST))
            display = Bukkit.createInventory(this, size, title);
        else
            display = Bukkit.createInventory(this, type, title);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return display;
    }

    //Setting item
    public void renderElement(int slot, MenuElement element) {
        ElementStyle elementStyle = styleSheet.getStyle(element.getId());

        if (slot < 1) slot = 1;

        ItemStack item = elementStyle.getAttribute(TextureAttribute.class).get().clone();

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(MenuAPIConstants.uiElementInventoryTag(), PersistentDataType.STRING, element.getId());
        String title = color(elementStyle.preprocess(elementStyle.getAttribute(Title.class).get()));

        List<String> lore = new ArrayList<>(elementStyle.getAttribute(Lore.class)
                .get().stream()
                .map(elementStyle::preprocess)
                .map(MenuPanel::color)
                .toList());

        if (lore.isEmpty() && meta.getLore() != null) lore.addAll(meta.getLore());
        if (title.isEmpty() || title.isBlank()) title = meta.getDisplayName();

        meta.setDisplayName(title);
        meta.setLore(lore);
        item.setItemMeta(meta);

        display.setItem(slot - 1, item);
    }

    private static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public void refresh(boolean rerender) {
        if (!shown) return;
        final int menuSize = styleSheet.getHeadStyle().getMenuSize() * styleSheet.getHeadStyle().getMenuLookType().getElementsMinimum();
        if (rerender) {
            //Phase #1 Setting menu's properties
            String menuTitle = styleSheet.getHeadStyle().getMenuTitle().get();
            InventoryType inventoryType = styleSheet.getHeadStyle().getMenuLookType().getInventoryType();
            setCanvas(menuTitle, menuSize, inventoryType);
        }
        //Phase #3 Render elements
        menuDOM.entrySet().forEach(entry -> {
            MenuElement element = entry.getValue();
            renderElement(element.getSlot(), element);
        });
        if (!rerender)
            viewer.updateInventory();
    }

    public void addElement(MenuElement element, ElementStyle style) {
        int freeSlot = getFreeSlot();
        element.setSlot(freeSlot);

        menuDOM.addElement(element);

        styleSheet.setStyle(element.getId(), style);
    }


}
