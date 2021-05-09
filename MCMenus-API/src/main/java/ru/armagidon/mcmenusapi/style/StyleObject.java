package ru.armagidon.mcmenusapi.style;

import lombok.ToString;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import ru.armagidon.mcmenusapi.menu.MenuDisplay;


public interface StyleObject
{

    String process(String input, MenuDisplay context);


    @ToString
    class PlaceHolderedStyle implements StyleObject {

        @Override
        public String process(String input, MenuDisplay context) {
            return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(context.getViewer(), input));
        }
    }

    @ToString
    class StaticStyle implements StyleObject {

        @Override
        public String process(String input, MenuDisplay context) {
            return ChatColor.translateAlternateColorCodes('&', input);
        }
    }
}
