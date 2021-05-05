package ru.armagidon.mcmenusapi;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.menu.MenuDisplay;

import java.util.HashMap;
import java.util.Map;

public class Menu
{
    private final Map<String, MenuPanel> panels = new HashMap<>();
    private final Map<Player, MenuDisplay> menuViewers = new HashMap<>();

    public void open(Player player, String defaultPanel) {
        MenuDisplay display = menuViewers.computeIfAbsent(player, p -> new MenuDisplay(this, p));
        display.show(panels.get(defaultPanel));
    }

    public void closeInventory(Player player) {
        menuViewers.remove(player);
    }

    public void addPanel(String id, MenuPanel panel) {
        Validate.notEmpty(id);
        panels.put(id, panel);
    }

    public MenuPanel getPanel(String id) {
        return panels.get(id);
    }


}
