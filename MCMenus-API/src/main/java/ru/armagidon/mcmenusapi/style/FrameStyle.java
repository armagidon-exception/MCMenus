package ru.armagidon.mcmenusapi.style;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.armagidon.mcmenusapi.menu.MenuCanvas;
import ru.armagidon.mcmenusapi.misc.RangeConstrainedInt;
import ru.armagidon.mcmenusapi.style.attributes.Attribute;
import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;
import ru.armagidon.mcmenusapi.style.attributes.Title;

import java.util.function.Function;

import static ru.armagidon.mcmenusapi.style.AttributeParser.createParser;

public class FrameStyle extends Style
{
    private static final int DEFAULT_MENU_SIZE = 1;

    private static final Function<ru.armagidon.mcmenusapi.parser.tags.LookAndFeelAttribute, LookAndFeelProperties> LOOK_AND_FEEL_PARSER = (input) -> new LookAndFeelProperties(input.size(), input.lookType());


    public FrameStyle() {
        super(new LookAndFeelAttribute(new LookAndFeelProperties(DEFAULT_MENU_SIZE, MenuLookType.NORMAL)), Title.of(""));
    }

    public static final class LookAndFeelAttribute extends Attribute.ParsedAttribute<ru.armagidon.mcmenusapi.parser.tags.LookAndFeelAttribute, LookAndFeelProperties>
    {
        public LookAndFeelAttribute(LookAndFeelProperties defaultValue) {
            super(defaultValue, createParser(ru.armagidon.mcmenusapi.parser.tags.LookAndFeelAttribute.class, LOOK_AND_FEEL_PARSER), (attribute, usePreprocessor, input) -> {
                if (input instanceof MenuCanvas canvas) {
                    canvas.setType(attribute.get().getLookType().getInventoryType());
                    canvas.setSize(attribute.get().calculateMenuSize());
                }
            });
        }
    }


    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static final class LookAndFeelProperties {

        private static final int ROW_SIZE = 9;

        RangeConstrainedInt menuSize;
        @Getter MenuLookType lookType;

        public LookAndFeelProperties(int menuSize, MenuLookType lookType) {
            this.menuSize = new RangeConstrainedInt(false, 1, 6,  menuSize);
            this.lookType = lookType;
        }

        public int getMenuSize() {
            return menuSize.get();
        }

        public int calculateMenuSize() {
            if (lookType.equals(MenuLookType.NORMAL))
                return menuSize.get() * ROW_SIZE;
            else
                return lookType.getElementsMinimum();
        }
    }
}
