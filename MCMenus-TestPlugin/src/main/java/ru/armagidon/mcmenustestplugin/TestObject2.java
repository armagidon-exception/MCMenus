package ru.armagidon.mcmenustestplugin;


import ru.armagidon.mcmenusapi.parser.tags.CheckBoxTag;
import ru.armagidon.mcmenusapi.parser.tags.ItemTexturePath;
import ru.armagidon.mcmenusapi.parser.tags.LookAndFeel;
import ru.armagidon.mcmenusapi.parser.tags.TitlePath;
import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;

@TitlePath(title = "Test object 2", isPath = false)
@LookAndFeel(lookType = MenuLookType.NORMAL)
public class TestObject2
{
    @TitlePath(title = "Test checkbox", isPath = false)
    @ItemTexturePath(path = "checkbox")
    @CheckBoxTag(checked = false, checkStatePath = "checked_texture")
    private boolean testCheckButton = false;
}
