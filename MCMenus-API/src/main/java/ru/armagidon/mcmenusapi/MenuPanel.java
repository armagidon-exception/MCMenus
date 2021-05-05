package ru.armagidon.mcmenusapi;

import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import ru.armagidon.mcmenusapi.menu.MenuDisplay;
import ru.armagidon.mcmenusapi.menuelements.MenuElement;
import ru.armagidon.mcmenusapi.menuelements.MenuModel;
import ru.armagidon.mcmenusapi.style.Lore;
import ru.armagidon.mcmenusapi.style.MenuStyleSheet;
import ru.armagidon.mcmenusapi.style.Style;
import ru.armagidon.mcmenusapi.style.Title;

@ToString
public class MenuPanel
{

    public static final int MENU_WIDTH = 9;

    @Setter private MenuModel model;
    @Setter private MenuStyleSheet styleSheet;

    public MenuPanel() {
        styleSheet = new MenuStyleSheet(this);
        model = new MenuModel();
    }

    public void render(MenuDisplay context) {
        //Phase #1 Setting size of the menu
        context.setDisplay(Bukkit.createInventory(context, styleSheet.getSize() * MENU_WIDTH, ChatColor.translateAlternateColorCodes('&', styleSheet.getTitle())));
        //Phase #2 Putting elements
        model.entrySet().forEach(entry -> {
            Style style = styleSheet.getStyle(entry.getKey());
            MenuElement element = entry.getValue();

            if (style.getX() > 8 || style.getY() > styleSheet.getSize()) return;

            element.render(style, context.getInventory());
        });
    }

    public void reRender(MenuDisplay context) {
        model.entrySet().forEach(entry -> {
            Style style = styleSheet.getStyle(entry.getKey());
            MenuElement element = entry.getValue();

            if (style.getX() > 8 || style.getY() > styleSheet.getSize()) return;

            element.render(style, context.getInventory());
        });
    }

    public void addElement(MenuElement element) {
        model.addElement(element);
        styleSheet.setStyle(element.getId(), new Style(Title.of(""), Lore.of("")));
    }

    public MenuModel getModel() {
        return model;
    }

    public MenuStyleSheet getStyleSheet() {
        return styleSheet;
    }
}
