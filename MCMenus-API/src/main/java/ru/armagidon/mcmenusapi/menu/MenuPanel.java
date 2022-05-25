package ru.armagidon.mcmenusapi.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import ru.armagidon.mcmenusapi.menu.container.Container;
import ru.armagidon.mcmenusapi.menu.elements.DivisionBlock;
import ru.armagidon.mcmenusapi.menu.elements.MenuDOM;
import ru.armagidon.mcmenusapi.menu.elements.MenuElement;
import ru.armagidon.mcmenusapi.menu.layout.BlockLayout;
import ru.armagidon.mcmenusapi.menu.layout.Dimension;
import ru.armagidon.mcmenusapi.menu.layout.Layout;
import ru.armagidon.mcmenusapi.menu.layout.Position;
import ru.armagidon.mcmenusapi.style.FrameStyle;
import ru.armagidon.mcmenusapi.style.MenuStyleSheet;
import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuPanel implements InventoryHolder, Container
{

    MenuDOM menuDOM;
    MenuStyleSheet styleSheet;
    @Getter String id;
    @Getter Player viewer;
    @NonFinal final MenuCanvas canvas;
    @NonFinal volatile boolean shown;
    @Getter @NonFinal volatile Layout layout;

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
            menuDOM.entrySet().stream().map(Map.Entry::getKey).map(styleSheet::getStyle).filter(Objects::nonNull).forEach(style ->
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

    @NotNull
    @Override
    public Inventory getInventory() {
        return canvas.getDisplay();
    }

    //Setting item
    public void renderElement(Position position, MenuElement element) {
        element.render(position, this);
    }

    public void refresh(boolean rerender) {
        if (!shown) return;
        if (rerender) {
            //Phase #1 Setting menu's properties
            styleSheet.getFrameStyle().applyAttributes(canvas);
        }

        //Phase #3 Render elements
        FrameStyle.LookAndFeelProperties properties = getStyleSheet().getFrameStyle().getAttribute(FrameStyle.LookAndFeelAttribute.class).get();
        layout.apply(menuDOM.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toUnmodifiableSet()), new Position(), new Dimension(properties.getLookType().getElementsMinimum(), properties.getMenuSize()))
                .forEach((key, value) -> renderElement(value, key));
        if (!rerender)
            viewer.updateInventory();
    }

    public void addElement(MenuElement element) {
        menuDOM.addElement(element);
    }

    @Override
    public MenuElement getElement(String id) {
        MenuElement foundElement = menuDOM.getElement(id);
        if (foundElement == null) {
            Optional<MenuElement> nestedElement = getChildren().stream()
                    .filter(e -> e instanceof DivisionBlock)
                    .map(e -> (DivisionBlock) e).filter(d -> d.getElement(id) != null)
                    .map(d -> d.getElement(id)).findFirst();
            return nestedElement.orElse(null);
        }
        return foundElement;
    }

    @Override
    public Set<MenuElement> getChildren() {
        return menuDOM.entrySet().stream()
                .map(Map.Entry::getValue).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public MenuCanvas getCanvas() {
        return canvas;
    }
}
