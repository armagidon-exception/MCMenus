package ru.armagidon.mcmenusapi.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuPanel implements InventoryHolder
{

    final MenuDOM menuDOM;
    final MenuStyleSheet styleSheet;
    @Getter final String id;
    volatile Inventory display;
    final Player viewer;
    volatile boolean shown;

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


    public void render() {
        refresh(true);
    }

    public void addElement(MenuElement element) {
        ElementStyle elementStyle = new ElementStyle(Title.of(""), Lore.of(""));
        addElement(element, elementStyle);
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
    public synchronized void setCanvas(String title, int size, InventoryType type) {
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
    public void setElement(int slot, MenuElement element) {
        ElementStyle elementStyle = styleSheet.getStyle(element.getId());

        if (slot < 1) slot = 1;

        ItemStack item = element.getItem();
        if (item == null) {
            item = new ItemStack(elementStyle.getAttribute(TextureAttribute.class).get());
        }

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(MenuAPIConstants.uiElementInventoryTag(), PersistentDataType.STRING, element.getId());
        String title = styleSheet.getHeadStyle()
                .getPlaceHolderProcessor()
                .apply(new PlaceholderProcessingContext(viewer), color(elementStyle.getAttribute(Title.class).get()));

        List<String> lore = elementStyle.getAttribute(Lore.class)
                .get().stream().map(MenuPanel::color)
                .map(s -> styleSheet.getHeadStyle().getPlaceHolderProcessor().apply(new PlaceholderProcessingContext(viewer), s))
                .toList();
        if (lore.isEmpty()) lore.addAll(meta.getLore());
        if (title.isEmpty() || title.isBlank()) title = meta.getDisplayName();

        meta.setDisplayName(title);
        meta.setLore(lore);
        item.setItemMeta(meta);

        display.setItem(slot - 1, element.getItem());
        viewer.updateInventory();
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
        } else {
            display.clear();
        }
        //Phase #2 Fixing problems
        //Fixing out-of-bounds position of the elements
        menuDOM.entrySet().stream().map(Map.Entry::getValue)
                .forEach(elementStyle -> elementStyle.setSlot(getFreeSlot()));
        //Checking if some elements share the same slot
        Map<Integer, List<String>> sortedSlots = new TreeMap<>();
        menuDOM.entrySet().stream().map(Map.Entry::getValue)
                .forEach(element -> {
                    List<String> s = sortedSlots.computeIfAbsent(element.getSlot(), (i) -> new LinkedList<>());
                    s.add(element.getId());
                });

        for (List<String> repeatingElements : sortedSlots.values()) {
            if (repeatingElements.size() > 1) {
                int counter = 0;
                LinkedList<MenuElement> elements = repeatingElements.stream().map(menuDOM::getElement).collect(Collectors.toCollection(LinkedList::new));
                for (MenuElement menuElement : elements) {
                    int slot  = menuElement.getSlot() + (counter++);
                    if (slot >= menuSize) {
                        menuElement.setSlot(getFreeSlot());
                    } else
                        menuElement.setSlot(slot);
                }
            }
        }
        //Phase #3 Putting elements
        menuDOM.entrySet().forEach(entry -> {
            MenuElement element = entry.getValue();
            setElement(element.getSlot(), element);
        });
    }

    public void addElement(MenuElement element, ElementStyle style) {
        int freeSlot = getFreeSlot();

        menuDOM.addElement(element);

        element.setSlot(freeSlot);

        styleSheet.setStyle(element.getId(), style);
    }
}
