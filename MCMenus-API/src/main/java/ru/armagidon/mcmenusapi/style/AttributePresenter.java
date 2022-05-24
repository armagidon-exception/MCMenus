package ru.armagidon.mcmenusapi.style;

import org.bukkit.ChatColor;
import ru.armagidon.mcmenusapi.menu.Renderable;
import ru.armagidon.mcmenusapi.style.attributes.Attribute;

import java.util.function.UnaryOperator;

public interface AttributePresenter<A extends Attribute<?>>
{

    void present(A attribute, UnaryOperator<String> usePreprocessor, Renderable input);

    static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }



}
