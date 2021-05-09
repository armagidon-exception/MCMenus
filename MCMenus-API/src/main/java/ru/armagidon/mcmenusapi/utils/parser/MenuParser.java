package ru.armagidon.mcmenusapi.utils.parser;

import com.google.common.collect.ImmutableSet;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.style.Title;
import ru.armagidon.mcmenusapi.utils.parser.tags.PanelTag;

import java.util.HashMap;
import java.util.Map;

public class MenuParser {

    static final Map<NamespacedKey, ItemStack> textureRegistry = new HashMap<>();

    private static final ImmutableSet<ElementParser> parsers = ImmutableSet.<ElementParser>builder().add(new ButtonParser(), new LinkParser(), new CheckBoxParser()).build();

    public static MenuPanel objectToPanel(Plugin plugin, Object obj) {
        Class<?> objectClass = obj.getClass();

        if (!objectClass.isAnnotationPresent(PanelTag.class))
            throw new IllegalArgumentException("Object should annotated as panel with @Panel annotation");

        PanelTag panelData = objectClass.getDeclaredAnnotation(PanelTag.class);

        MenuPanel panel = new MenuPanel(panelData.id());
        Title title = Title.of(panelData.title());
        panel.getStyleSheet().setTitle(title);

        parsers.forEach(parser -> parser.parse(plugin, panel, obj));

        return panel;
    }

    public static void registerTexture(Plugin plugin, String name, ItemStack stack) {
        textureRegistry.put(of(plugin, name), stack);
    }

    static NamespacedKey of(Plugin plugin, String s) {
        return new NamespacedKey(plugin, s);
    }
}
