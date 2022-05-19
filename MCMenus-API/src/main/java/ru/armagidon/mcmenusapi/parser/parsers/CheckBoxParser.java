package ru.armagidon.mcmenusapi.parser.parsers;

import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.elements.Button;
import ru.armagidon.mcmenusapi.elements.MenuElement;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.misc.jool.Unchecked;
import ru.armagidon.mcmenusapi.parser.ElementParser;
import ru.armagidon.mcmenusapi.parser.ElementParsingContext;
import ru.armagidon.mcmenusapi.parser.tags.CheckBoxTag;
import ru.armagidon.mcmenusapi.style.ElementStyle;
import ru.armagidon.mcmenusapi.style.attributes.TextureAttribute;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;

public class CheckBoxParser implements ElementParser<Field>
{

    @Override
    public MenuElement parse(ElementParsingContext<Field> input) {
        return new Button(input.getInput().getName(), Unchecked.consumer((button) ->
        {
            input.getInput().setAccessible(true);
            boolean currentValue = input.getInput().getBoolean(input.getDataModel());
            input.getInput().setBoolean(input.getDataModel(), !currentValue);

            CheckBoxTag checkBoxTag = input.getInput().getAnnotation(CheckBoxTag.class);
            String checkStatePath = checkBoxTag.checkStatePath();
            MenuPanel parent = input.getOwner().getPanel((String) input.getAdditionalData()[0]);
            ElementStyle style = parent.getStyleSheet().getStyle(button.getId());

            if (currentValue) {
                style.getAttribute(TextureAttribute.class).reset();
            } else {
                style.getAttribute(TextureAttribute.class).set(MCMenusAPI.getItemTextureRegistry().getByPath(checkStatePath));
            }

        }));
    }

    @Override
    public ElementStyle parseStyle(Field input) {
        ElementStyle style = ElementParser.super.parseStyle(input);
        CheckBoxTag checkBoxTag = input.getAnnotation(CheckBoxTag.class);

        if (checkBoxTag.checked()) {
            style.getAttribute(TextureAttribute.class).set(MCMenusAPI.getItemTextureRegistry().getByPath(checkBoxTag.checkStatePath()));
        }
        return style;
    }

    @Override
    public ElementType mayBeAttachedTo() {
        return ElementType.FIELD;
    }

    @Override
    public Class<?>[] supportedTypes() {
        return new Class[] {Boolean.class, boolean.class};
    }
}
