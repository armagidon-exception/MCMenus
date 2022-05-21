package ru.armagidon.mcmenusapi.parser.parsers;

import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.data.ElementParsingContext;
import ru.armagidon.mcmenusapi.data.StyleParsingContext;
import ru.armagidon.mcmenusapi.elements.Button;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.misc.jool.Unchecked;
import ru.armagidon.mcmenusapi.parser.ElementParser;
import ru.armagidon.mcmenusapi.parser.tags.CheckBoxTag;
import ru.armagidon.mcmenusapi.style.ElementStyle;
import ru.armagidon.mcmenusapi.style.attributes.TextureAttribute;

import java.lang.annotation.ElementType;
import java.util.UUID;

public class CheckBoxParser implements ElementParser<CheckBoxTag, Boolean> {

    @Override
    public MenuElement parse(ElementParsingContext<CheckBoxTag, Boolean> input) {
        Button button =  new Button(UUID.randomUUID().toString(), Unchecked.consumer((btn) ->
        {
            boolean currentValue = input.getDataGetter().get();
            input.getDataSetter().accept(!currentValue);

        }));

        return button;
    }

    @Override
    public ElementStyle parseStyle(StyleParsingContext<Boolean> input) {
        ElementStyle style = ElementParser.super.parseStyle(input);

        CheckBoxTag checkBoxTag = input.getData(CheckBoxTag.class);
        String checkStatePath = checkBoxTag.checkStateTexturePath();

        style.getAttribute(TextureAttribute.class).setUpdateFunction(attribute -> {
            if (input.getDataGetter().get()) {
                style.getAttribute(TextureAttribute.class).set(MCMenusAPI.getItemTextureRegistry().getByPath(checkStatePath));
            } else {
                style.getAttribute(TextureAttribute.class).reset();
            }
        });

        style.addPreprocessorUnit((i) -> {
            boolean state = input.getDataGetter().get();
            return i.replace("%checkbox_state%", Boolean.toString(state));
        });
        return style;
    }

    @Override
    public ElementType mayBeAttachedTo() {
        return ElementType.FIELD;
    }


    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends Boolean>[] supportedTypes() {
        return new Class[] {Boolean.class, boolean.class};
    }
}
