package ru.armagidon.mcmenustestplugin;

import org.bukkit.event.inventory.InventoryClickEvent;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.utils.parser.tags.ButtonTag;
import ru.armagidon.mcmenusapi.utils.parser.tags.CheckBoxTag;
import ru.armagidon.mcmenusapi.utils.parser.tags.LinkTag;
import ru.armagidon.mcmenusapi.utils.parser.tags.PanelTag;

@PanelTag(id = "test_panel", title = "&3Menu of player %player_name%")
public class TestObject
{
    @LinkTag(id = "test_link", texture = "link")
    private final MenuPanel panel;

    @CheckBoxTag(id = "test_checkbox", texture = "checkbox")
    private boolean state;

    public TestObject(MenuPanel panel) {
        this.panel = panel;
    }

    @ButtonTag(id = "test_button", texture = "barrel")
    public void testMethod(InventoryClickEvent event) {
        event.getWhoClicked().sendMessage("Hello there!");
    }

    @ButtonTag(id = "test_button2", texture = "barrier")
    public void testMethod2(InventoryClickEvent event) {
        event.getWhoClicked().sendMessage("Hello there 2!");
    }
}
