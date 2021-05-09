package ru.armagidon.mcmenusapi.utils.parser;

import org.bukkit.plugin.Plugin;
import ru.armagidon.mcmenusapi.menu.MenuPanel;

public interface ElementParser
{
    void parse(Plugin plugin, MenuPanel context, Object src);
}
