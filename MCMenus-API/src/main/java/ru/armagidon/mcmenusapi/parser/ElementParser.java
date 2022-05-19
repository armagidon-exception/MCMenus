package ru.armagidon.mcmenusapi.parser;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.parser.tags.ItemTexturePath;
import ru.armagidon.mcmenusapi.parser.tags.LorePath;
import ru.armagidon.mcmenusapi.parser.tags.TitlePath;
import ru.armagidon.mcmenusapi.style.ElementStyle;
import ru.armagidon.mcmenusapi.style.attributes.Lore;
import ru.armagidon.mcmenusapi.style.attributes.TextureAttribute;
import ru.armagidon.mcmenusapi.style.attributes.Title;

import java.lang.annotation.ElementType;
import java.lang.reflect.AccessibleObject;
import java.util.Arrays;

public interface ElementParser<I extends AccessibleObject>
{
    MenuElement parse(ElementParsingContext<I> input);

    default ElementStyle parseStyle(ElementParsingContext<I> input) {
        Title title = Title.of("");
        Lore lore = Lore.of();
        if (input.getInput().isAnnotationPresent(TitlePath.class)) {
            TitlePath titlePath = input.getInput().getAnnotation(TitlePath.class);
            if (titlePath.isPath()) {
                title.set(MCMenusAPI.getTitleRegistry().getByPath(titlePath.title()));
            } else {
                title.set(titlePath.title());
            }
        }
        if (input.getInput().isAnnotationPresent(LorePath.class)) {
            LorePath lorePath = input.getInput().getAnnotation(LorePath.class);
            if (lorePath.isPath()) {
                lore.set(MCMenusAPI.getItemLoreRegistry().getByPath(lorePath.lore()[0]));
            } else {
                lore.set(Arrays.asList(lorePath.lore()));
            }
        }
        TextureAttribute textureAttribute = TextureAttribute.of(Material.STONE);
        if (input.getInput().isAnnotationPresent(ItemTexturePath.class)) {
            ItemTexturePath texturePath = input.getInput().getAnnotation(ItemTexturePath.class);
            textureAttribute = TextureAttribute.of(MCMenusAPI.getItemTextureRegistry().getByPath(texturePath.path()));
        }

        ElementStyle style =  new ElementStyle(title, lore, textureAttribute);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            style.addPreprocessorUnit((i) -> PlaceholderAPI.setPlaceholders(input.getOwner().getViewer(), i));
        }

        return style;
    }

    ElementType mayBeAttachedTo();

    default Class<?>[] supportedTypes() {
        return new Class<?>[0];
    }
}
