package ru.armagidon.mcmenustestplugin;


import ru.armagidon.mcmenusapi.parser.tags.*;
import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;

@TitleAttribute(title = "Test object 2", isPath = false)
@LookAndFeelAttribute(lookType = MenuLookType.NORMAL, size = 4)
public class TestObject2
{
    @TitleAttribute(title = "Test checkbox", isPath = false)
    @ItemTextureAttribute(value = "checkbox")
    @LoreAttribute(lore = {"checked: %checkbox_state%"}, isPath = false)
    @CheckBoxTag(checkStateTexturePath = "checked_texture")
    private boolean testCheckButton = false;


    @BlockLayout(width = 3, height = 3)
    @TitleAttribute(title = "Test checkbox 1", isPath = false)
    @ItemTextureAttribute(value = "checkbox")
    @LoreAttribute(lore = {"checked: %checkbox_state%"}, isPath = false)
    @CheckBoxTag(checkStateTexturePath = "checked_texture")
    public boolean[] generateCheckboxes = new boolean[] {false, false, false, false, false, false, false, false, false};

}
