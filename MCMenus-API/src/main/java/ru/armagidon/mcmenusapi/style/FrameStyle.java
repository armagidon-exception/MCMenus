package ru.armagidon.mcmenusapi.style;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.armagidon.mcmenusapi.misc.RangeConstrainedInt;
import ru.armagidon.mcmenusapi.parser.tags.LookAndFeel;
import ru.armagidon.mcmenusapi.style.attributes.Attribute;
import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;
import ru.armagidon.mcmenusapi.style.attributes.Title;

public class FrameStyle extends Style
{
    private static final int DEFAULT_MENU_SIZE = 1;

    private static final AttributeParser<LookAndFeelProperties> LOOK_AND_FEEL_PARSER = (target, input) -> {
        if (input.isAnnotationPresent(LookAndFeel.class)) {
            LookAndFeel lookAndFeel = input.getData(LookAndFeel.class);
            target.setDefault(new LookAndFeelProperties(lookAndFeel.size(),lookAndFeel.lookType()));
        }
    };

    public FrameStyle() {
        super(new LookAndFeelAttribute(new LookAndFeelProperties(DEFAULT_MENU_SIZE, MenuLookType.NORMAL)), Title.of(""));
    }

    public static final class LookAndFeelAttribute extends Attribute.SimpleAttribute<LookAndFeelProperties>
    {
        public LookAndFeelAttribute(LookAndFeelProperties defaultValue) {
            super(defaultValue, LOOK_AND_FEEL_PARSER);
        }
    }


    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static final class LookAndFeelProperties {
        RangeConstrainedInt menuSize;
        @Getter MenuLookType lookType;

        public LookAndFeelProperties(int menuSize, MenuLookType lookType) {
            this.menuSize = new RangeConstrainedInt(false, 1, 6,  menuSize);
            this.lookType = lookType;
        }

        public int getMenuSize() {
            return menuSize.get();
        }
    }
}
