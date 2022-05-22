package ru.armagidon.mcmenustestplugin;

import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.parser.tags.*;
import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;

@LookAndFeel(lookType = MenuLookType.NORMAL, size = 6)
@TitlePath(title = "&3Menu of player %player_name%", isPath = false)
public class TestObject
{

    @LinkTag
    @ItemTexturePath(path = "arrow")
    @TitlePath(title = "Hello, %player_name%", isPath = false)
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

    public void run() {

    }
}
