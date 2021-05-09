package ru.armagidon.mcmenusapi.elements;

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.menu.MenuDisplay;

public class Link extends MenuElement
{

    private final String locationURL;
    private final MenuPanel locationPanel;

    public Link(String id, ItemStack stack, String location) {
        super(id, stack);
        Validate.notEmpty(location);
        this.locationURL = location;
        this.locationPanel = null;
    }

    public Link(String id, ItemStack stack, MenuPanel location) {
        super(id, stack);
        Validate.notNull(location);
        this.locationPanel = location;
        this.locationURL = null;
    }

    @Override
    public void handleClickEvent(MenuDisplay display) {
        if (display == null) return;
        MenuPanel panel;
        if (locationURL != null) {
            panel = display.getContext().getParent().getPanel(locationURL);
        } else {
            panel = locationPanel;
        }
        display.setCurrentPanel(panel);
    }
}
