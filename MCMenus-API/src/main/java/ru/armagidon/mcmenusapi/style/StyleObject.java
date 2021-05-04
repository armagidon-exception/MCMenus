package ru.armagidon.mcmenusapi.style;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;

import java.util.Map;

public interface StyleObject
{

    String process(String input);


    @AllArgsConstructor
    class PlaceHolderedStyle implements StyleObject {

        private final Map<String, String> placeholders;

        @Override
        public String process(String input) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                String placeholder = entry.getKey();
                String value = entry.getValue();

                input = input.replace(placeholder, value);
            }
            return ChatColor.translateAlternateColorCodes('&', input);
        }
    }

    class StaticStyle implements StyleObject {

        @Override
        public String process(String input) {
            return ChatColor.translateAlternateColorCodes('&', input);
        }
    }
}
