package ru.armagidon.mcmenusapi.parsers;

import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.parsers.tags.ItemTexturePath;
import ru.armagidon.mcmenusapi.parsers.tags.LorePath;
import ru.armagidon.mcmenusapi.parsers.tags.TitlePath;
import ru.armagidon.mcmenusapi.style.ElementStyle;
import ru.armagidon.mcmenusapi.style.attributes.Lore;
import ru.armagidon.mcmenusapi.style.attributes.TextureAttribute;
import ru.armagidon.mcmenusapi.style.attributes.Title;

import java.lang.annotation.ElementType;
import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.List;

public interface ElementParser<I extends AccessibleObject>
{
    MenuElement parse(I input);

    default ElementStyle parseStyle(I input) {
        ElementStyle style = new ElementStyle();
        if (input.isAnnotationPresent(TitlePath.class)) {
            TitlePath title = input.getAnnotation(TitlePath.class);
            if (title.isPath()) {
                style.getAttribute(Title.class).set(MCMenusAPI.getTitleRegistry().getByPath(title.title()));
            } else {
                style.getAttribute(Title.class).set(title.title());
            }
        }
        if (input.isAnnotationPresent(LorePath.class)) {
            LorePath lore = input.getAnnotation(LorePath.class);
            if (lore.isPath()) {
                style.getAttribute(Lore.class).set(List.of(MCMenusAPI.getTitleRegistry().getByPath(lore.lore()[0])));
            } else {
                style.getAttribute(Lore.class).set(Arrays.asList(lore.lore()));
            }
        }
        if (input.isAnnotationPresent(ItemTexturePath.class)) {
            ItemTexturePath texturePath = input.getAnnotation(ItemTexturePath.class);
            style.getAttribute(TextureAttribute.class).set(MCMenusAPI.getItemTextureRegistry().getByPath(texturePath.path()));
        }
        return style;
    }
    ElementType supportedType();
}
