package ru.armagidon.mcmenusapi.style.attributes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.menu.elements.RenderedElement;
import ru.armagidon.mcmenusapi.parser.tags.ItemTextureAttribute;
import ru.armagidon.mcmenusapi.style.AttributePresenter;

import java.util.function.Function;

import static ru.armagidon.mcmenusapi.style.AttributeParser.createParser;


public class TextureAttribute extends Attribute.ParsedAttribute<ItemTextureAttribute, ItemStack> {

    private static final Function<ItemTextureAttribute, ItemStack> PARSER = (input) ->
            MCMenusAPI.getItemTextureRegistry().getByPath(input.value());

    private static final AttributePresenter<Attribute<ItemStack>> PRESENTER = (attribute, usePreprocessor, input) -> {
        if (input instanceof RenderedElement renderedElement) {
            renderedElement.setItem(attribute.get());
        }
    };

    private TextureAttribute(ItemStack defaultValue) {
        super(defaultValue.clone(), createParser(ItemTextureAttribute.class, PARSER), PRESENTER);
    }

    public static TextureAttribute of(ItemStack texture) {
        return new TextureAttribute(texture);
    }

    public static TextureAttribute of(Material texture) {
        return of(new ItemStack(texture));
    }
}
