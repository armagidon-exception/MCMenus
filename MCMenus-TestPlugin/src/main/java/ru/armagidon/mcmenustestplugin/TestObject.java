package ru.armagidon.mcmenustestplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.parser.tags.*;
import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;

@LookAndFeel(lookType = MenuLookType.NORMAL, size = 6)
@TitlePath(title = "&3Menu of player %player_name%", isPath = false)
public class TestObject implements TestObj
{

    @LinkTag
    @TitlePath(title = "Hello, %player_name%", isPath = false)
    @ItemTexturePath(path = "arrow")
    private final TestObject2 anotherObject;

    @TitlePath(title = "Test checkbox", isPath = false)
    @ItemTexturePath(path = "checkbox")
    @LorePath(lore = {"checked: %checkbox_state%"}, isPath = false)
    @CheckBoxTag(checkStateTexturePath = "checked_texture")
    private boolean testCheckButton = false;

    public TestObject(TestObject2 anotherObject) {
        this.anotherObject = anotherObject;
    }

    @ButtonTag
    @TitlePath(title = "&aHello there button", isPath = false)
    @ItemTexturePath(path = "barrier")
    public void testMethod(Player clicker) {
        clicker.sendMessage("Hello there!");
    }


    @DataModifier
    public void setTestCheckButton(boolean testCheckButton) {
        this.testCheckButton = testCheckButton;
    }

    public void run() {

    }
}
