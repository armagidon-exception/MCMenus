package ru.armagidon.mcmenusapi.menu;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Menu
{
    private final Map<String, MenuPanel> panels = new HashMap<>();

    public void open(Player player, String defaultPanel) {
        panels.get(defaultPanel).show(player);
    }

    public void addPanel(MenuPanel panel) {
        panels.computeIfAbsent(panel.getId(), (i) -> panel).setParent(this);
    }

    public MenuPanel getPanel(String id) {
        return panels.get(id);
    }


}
