package ru.armagidon.mcmenusapi.style;

import org.bukkit.ChatColor;
import ru.armagidon.mcmenusapi.menu.Renderable;
import ru.armagidon.mcmenusapi.style.attributes.Attribute;

import java.util.function.UnaryOperator;

public interface AttributePresenter<T>
{

    static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    void present(Attribute<T> attribute, UnaryOperator<String> usePreprocessor, Renderable input);
}
