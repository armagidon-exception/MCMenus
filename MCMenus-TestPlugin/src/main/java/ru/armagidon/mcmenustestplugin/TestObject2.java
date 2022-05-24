package ru.armagidon.mcmenustestplugin;


import ru.armagidon.mcmenusapi.parser.tags.*;
import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;

@TitleAttribute(title = "Test object 2", isPath = false)
@LookAndFeelAttribute(lookType = MenuLookType.NORMAL)
public class TestObject2
{
    @TitleAttribute(title = "Test checkbox", isPath = false)
    @ItemTextureAttribute(value = "checkbox")
    @LoreAttribute(lore = {"checked: %checkbox_state%"}, isPath = false)
    @CheckBoxTag(checkStateTexturePath = "checked_texture")
    private boolean testCheckButton = false;


    @TitleAttribute(title = "Test checkbox", isPath = false)
    @ItemTextureAttribute(value = "checkbox")
    @LoreAttribute(lore = {"checked: %checkbox_state%"}, isPath = false)
    @CheckBoxTag(checkStateTexturePath = "checked_texture")
    public Boolean[] generateCheckboxes = new Boolean[] {false, false, false};

}
