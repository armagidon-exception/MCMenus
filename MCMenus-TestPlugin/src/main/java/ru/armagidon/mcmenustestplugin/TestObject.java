package ru.armagidon.mcmenustestplugin;

import ru.armagidon.mcmenusapi.menu.MenuDisplay;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.utils.parser.tags.*;

@PanelTag(id = "test_panel", title = "&3Menu of player %player_name%")
public class TestObject
{

    @IconTag(id ="test_icon", texture = "icon")
    private final String name = "%player_name%";

    @LinkTag(id = "test_link", texture = "link")
    private final MenuPanel panel;

    @CheckBoxTag(id = "test_checkbox", texture = "checkbox")
    private boolean state;

    public TestObject(MenuPanel panel) {
        this.panel = panel;
    }

    @ButtonTag(id = "test_button", texture = "barrel")
    public void testMethod(MenuDisplay event) {
        event.getViewer().sendMessage("Hello there!");
    }
}
