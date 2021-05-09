package ru.armagidon.mcmenusapi.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.elements.MenuModel;
import ru.armagidon.mcmenusapi.style.Lore;
import ru.armagidon.mcmenusapi.style.MenuStyleSheet;
import ru.armagidon.mcmenusapi.style.Style;
import ru.armagidon.mcmenusapi.style.Title;

import java.util.*;
import java.util.stream.Collectors;

@ToString
public class MenuPanel
{

    public static final int MENU_WIDTH = 9;

    @Setter private MenuModel model;
    @Setter private MenuStyleSheet styleSheet;
    @Getter private final String id;

    private final Map<Player, MenuDisplay> viewers = new HashMap<>();

    @Setter(value = AccessLevel.PACKAGE)
    @Getter private Menu parent;

    public MenuPanel(String id) {
        Validate.notEmpty(id);
        styleSheet = new MenuStyleSheet(this);
        model = new MenuModel();
        this.id = id;
    }

    void show(Player player) {
        MenuDisplay display = viewers.computeIfAbsent(player, p -> new MenuDisplay(this, p));
        display.show(this);
    }

    public void handleClosure(Player player) {
        viewers.remove(player);
    }


    public void render(MenuDisplay context) {
        //Phase #1 Setting size of the menu
        styleSheet.getTitle().render(context);
        context.setDisplay(Bukkit.createInventory(context, styleSheet.getSize() * MENU_WIDTH, styleSheet.getTitle().getTitle()));
        //Phase #2 Fixing problems
        //Fixing out-of-bounds position of the elements
        model.entrySet().stream().map(Map.Entry::getValue).map(el -> styleSheet.getStyle(el.getId())).forEach(style -> {
            if (style.getSlot() >= styleSheet.getSize() * MENU_WIDTH) {
                style.setSlot(styleSheet.getSize() * MENU_WIDTH - 1);
            } else if (style.getSlot() < 0) {
                style.setSlot(0);
            }
        });
        //Checking if some elements share the same slot
        Map<Integer, List<String>> sortedSlots = new TreeMap<>();
        model.entrySet().stream().map(Map.Entry::getValue).forEach(el -> {
            Style style = styleSheet.getStyle(el.getId());
            List<String> s = sortedSlots.computeIfAbsent(style.getSlot(), (i) -> new LinkedList<>());
            s.add(el.getId());
        });

        for (List<String> list : sortedSlots.values()) {
            if (list.size() > 1) {
                int counter = 0;
                LinkedList<MenuElement> elements = list.stream().map(el -> model.getElement(el)).collect(Collectors.toCollection(LinkedList::new));
                for (MenuElement menuElement : elements) {
                    Style style = styleSheet.getStyle(menuElement.getId());
                    int slot  = style.getSlot() + (counter++);
                    if (slot >= styleSheet.getSize() * MENU_WIDTH) {
                        style.setSlot(getFreeSlot());
                    } else
                        style.setSlot(slot);
                }
            }
        }

        //Phase #3 Putting elements
        model.entrySet().forEach(entry -> {
            Style style = styleSheet.getStyle(entry.getKey());
            MenuElement element = entry.getValue();
            style.getTitle().render(context);
            style.getLore().render(context);
            element.render(style, context.getInventory());
        });
    }

    public void reRender(MenuDisplay context) {
        model.entrySet().forEach(entry -> {
            Style style = styleSheet.getStyle(entry.getKey());
            MenuElement element = entry.getValue();

            element.render(style, context.getInventory());
        });
    }

    public void update() {
        viewers.values().forEach(display -> display.refresh(MenuDisplay.FULL_RERENDER));
    }

    public void addElement(MenuElement element) {
        int freeSlot = getFreeSlot();

        model.addElement(element);

        Style style = new Style(Title.of(""), Lore.of(""));
        style.setSlot(freeSlot);

        styleSheet.setStyle(element.getId(), style);
    }

    public MenuModel getModel() {
        return model;
    }

    public MenuStyleSheet getStyleSheet() {
        return styleSheet;
    }












    private int getFreeSlot() {
        Set<Integer> occupiedSlots = model.entrySet().stream().map(Map.Entry::getValue).map(el ->
                styleSheet.getStyle(el.getId()).getSlot()).sorted().collect(Collectors.toCollection(LinkedHashSet::new));

        Set<Integer> slots = new HashSet<>();
        for (int i = 0; i < MENU_WIDTH * styleSheet.getSize(); i++) {
            slots.add(i);
        }

        return slots.stream().filter(s -> !occupiedSlots.contains(s)).sorted().findFirst().get();
    }
}
