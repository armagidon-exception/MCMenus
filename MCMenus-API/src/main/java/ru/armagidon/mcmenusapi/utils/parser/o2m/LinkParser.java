package ru.armagidon.mcmenusapi.utils.parser.o2m;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.elements.Link;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.utils.parser.o2m.tags.LinkTag;

import java.lang.reflect.Field;
import java.util.Arrays;

class LinkParser implements ElementParser
{

    @Override
    public void parse(Plugin plugin, MenuPanel context, Object src) {
        Field[] fields = src.getClass().getDeclaredFields();

        Arrays.stream(fields).peek(f -> f.setAccessible(true)).filter(f -> f.isAnnotationPresent(LinkTag.class))
                .filter(f -> f.getType().equals(MenuPanel.class)).forEach(f -> {

            LinkTag linkData = f.getDeclaredAnnotation(LinkTag.class);

            ItemStack texture = MenuParser.textureRegistry.get(MenuParser.of(plugin, linkData.texture()));
            if (texture == null) {
                MCMenusAPI.getInstance().getLogger().severe("Texture " + linkData.texture() + " for link " + linkData.id() + " wasn't found in the registry. Skipping.");
                return;
            }

            try {
                MenuPanel location = (MenuPanel) f.get(src);

                Link link = new Link(linkData.id(), texture, location);
                context.addElement(link);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
