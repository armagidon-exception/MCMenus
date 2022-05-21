package ru.armagidon.mcmenustestplugin;


import ru.armagidon.mcmenusapi.parser.tags.*;
import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;

@TitlePath(title = "Test object 2", isPath = false)
@LookAndFeel(lookType = MenuLookType.NORMAL)
public class TestObject2
{
    @TitlePath(title = "Test checkbox", isPath = false)
    @ItemTexturePath(path = "checkbox")
    @LorePath(lore = {"checked: %checkbox_state%"}, isPath = false)
    @CheckBoxTag(checkStateTexturePath = "checked_texture")
    private boolean testCheckButton = false;

    @DataModifier
    public void setTestCheckButton(boolean testCheckButton) {
        this.testCheckButton = testCheckButton;
    }


}
