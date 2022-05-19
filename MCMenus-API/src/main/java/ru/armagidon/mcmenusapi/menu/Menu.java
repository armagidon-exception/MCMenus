package ru.armagidon.mcmenusapi.menu;

import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.armagidon.mcmenusapi.parser.parsers.MenuParser;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Menu
{
    Map<String, MenuPanel> panels = new ConcurrentHashMap<>();
    Map<MenuPanel, Object> objectModels = new ConcurrentHashMap<>();
    @Getter Player viewer;

    private Menu(Player viewer) {
        this.viewer = viewer;
    }

    public void addPanel(MenuPanel panel) {
        panels.putIfAbsent(panel.getId(), panel);
    }

    public MenuPanel createPanelAndAdd(String id) {
        var panel =  new MenuPanel(id, viewer);
        addPanel(panel);
        return panel;
    }

    public MenuPanel getPanel(String id) {
        return panels.get(id);
    }

    public Object getModelFor(MenuPanel panel) {
        return objectModels.get(panel);
    }

    public void setModelFor(MenuPanel panel, Object model) {
        objectModels.put(panel, model);
    }

    public Set<MenuPanel> panels() {
        return panels.values().stream().collect(Collectors.toUnmodifiableSet());
    }

    public static Menu convertAndOpenMenu(Plugin plugin, Player viewer, Object dataModel) {
        Menu menu = new Menu(viewer);
        Bukkit.getServer().getPluginManager().registerEvents(new SchemaMenuController(menu), plugin);
        MenuParser.convert(menu, viewer.getName(), dataModel).show();
        return menu;
    }


}
