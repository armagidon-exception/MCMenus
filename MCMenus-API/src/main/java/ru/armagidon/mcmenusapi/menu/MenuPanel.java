package ru.armagidon.mcmenusapi.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import ru.armagidon.mcmenusapi.menu.elements.MenuDOM;
import ru.armagidon.mcmenusapi.menu.elements.MenuElement;
import ru.armagidon.mcmenusapi.menu.elements.RenderedElement;
import ru.armagidon.mcmenusapi.misc.MenuAPIConstants;
import ru.armagidon.mcmenusapi.style.ElementStyle;
import ru.armagidon.mcmenusapi.style.FrameStyle;
import ru.armagidon.mcmenusapi.style.MenuStyleSheet;
import ru.armagidon.mcmenusapi.style.attributes.Lore;
import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;
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
    @Getter Player viewer;
    @NonFinal final MenuCanvas canvas;
    @NonFinal volatile boolean shown;

    public MenuPanel(String id, Player viewer) {
        Validate.notEmpty(id);
        this.canvas = new MenuCanvas(this);
        this.viewer = viewer;
        this.id = id;
        this.styleSheet = new MenuStyleSheet();
        this.menuDOM = new MenuDOM();
    }

    public synchronized void show() {
        shown = true;
        render();
        viewer.openInventory(canvas.getDisplay());
    }

    void close() {
        shown = false;
    }

    public boolean isShown() {
        return shown;
    }

    public void render() {
        if (!shown) return;
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            styleSheet.getFrameStyle().addPreprocessorUnit(input -> PlaceholderAPI.setPlaceholders(viewer, input));
            menuDOM.entrySet().stream().map(Map.Entry::getKey).map(styleSheet::getStyle).forEach(style ->
                    style.addPreprocessorUnit(input -> PlaceholderAPI.setPlaceholders(viewer, input)));
        }
        refresh(true);
    }

    public MenuDOM getMenuDOM() {
        return menuDOM;
    }

    public MenuStyleSheet getStyleSheet() {
        return styleSheet;
    }

    private int getFreeSlot() {
        final FrameStyle frameStyle = styleSheet.getFrameStyle();
        final FrameStyle.LookAndFeelProperties lookAndFeelProperties = frameStyle.getAttribute(FrameStyle.LookAndFeelAttribute.class).get();
        final MenuLookType lookAndFeel = lookAndFeelProperties.getLookType();
        final int columns = lookAndFeel.getElementsMinimum();
        int menuSize = getMenuSize(lookAndFeelProperties);
        if (!lookAndFeel.equals(MenuLookType.NORMAL))
            menuSize = columns;
        return IntStream.
                rangeClosed(1, menuSize)
                .filter(i -> menuDOM
                        .entrySet()
                        .stream()
                        .map(Map.Entry::getValue)
                        .map(MenuElement::getSlot)
                        .noneMatch(s -> s == i))
                .findFirst().getAsInt();
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return canvas.getDisplay();
    }

    //Setting item
    public void renderElement(int slot, MenuElement element) {
        ElementStyle elementStyle = styleSheet.getStyle(element.getId());

        if (slot < 1) slot = 1;

        RenderedElement renderedElement = RenderedElement.create(Material.STONE);
        elementStyle.applyAttributes(renderedElement);
        ItemStack item = renderedElement.asItemStack();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.getPersistentDataContainer().set(MenuAPIConstants.uiElementInventoryTag(), PersistentDataType.STRING, element.getId());
        item.setItemMeta(meta);

        canvas.setItem(slot - 1, item);
    }

    private int  getMenuSize(FrameStyle.LookAndFeelProperties lookAndFeelProperties) {
        final int rows = lookAndFeelProperties.getMenuSize();
        final MenuLookType lookAndFeel = lookAndFeelProperties.getLookType();
        final int columns = lookAndFeel.getElementsMinimum();
        return rows * columns;
    }

    public void refresh(boolean rerender) {
        if (!shown) return;
        final FrameStyle frameStyle = styleSheet.getFrameStyle();
        if (rerender) {
            //Phase #1 Setting menu's properties
            styleSheet.getFrameStyle().applyAttributes(canvas);
        }
        //Phase #3 Render elements
        menuDOM.entrySet().stream().map(Map.Entry::getValue).forEach(element ->
                renderElement(element.getSlot(), element));
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
