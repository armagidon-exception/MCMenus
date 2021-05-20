package ru.armagidon.mcmenusapi.utils.parser.o2m;

import com.google.common.reflect.TypeToken;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.elements.Icon;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.style.Lore;
import ru.armagidon.mcmenusapi.style.Style;
import ru.armagidon.mcmenusapi.style.Title;
import ru.armagidon.mcmenusapi.utils.parser.o2m.tags.IconTag;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class IndicatorParser implements ElementParser
{

    @Override
    public void parse(Plugin plugin, MenuPanel context, Object src) {
        Field[] fields = src.getClass().getDeclaredFields();

        Arrays.stream(fields).peek(f -> f.setAccessible(true)).filter(f -> f.isAnnotationPresent(IconTag.class))
                .filter(f -> f.getType().equals(String.class) || f.getType().equals(new TypeToken<List<String>>(){}.getRawType())).filter(f -> (f.getModifiers() & Modifier.STATIC) == 0).forEach(f -> {

            IconTag iconData = f.getDeclaredAnnotation(IconTag.class);

            ItemStack texture = MenuParser.textureRegistry.get(MenuParser.of(plugin, iconData.texture()));

            if (texture == null) {
                MCMenusAPI.getInstance().getLogger().severe("Texture " + iconData.texture() + " for icon " + iconData.id() + " wasn't found in the registry. Skipping.");
                return;
            }

            Title title = Title.of("");
            Lore lore = Lore.of();

            try {
                if (f.getType().equals(String.class)) {
                    title = Title.of((String) f.get(src));
                } else {
                    List<String> l = (List<String>) f.get(src);
                    lore = Lore.of(l.toArray(new String[0]));
                    title = Title.of("&f");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Style style = new Style(title, lore);

            Icon icon = new Icon(iconData.id(), texture);
            context.addElement(icon);

            context.getStyleSheet().setStyle(iconData.id(), style);

        });
    }
}
