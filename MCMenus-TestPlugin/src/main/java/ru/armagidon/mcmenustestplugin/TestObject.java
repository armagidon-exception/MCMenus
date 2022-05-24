package ru.armagidon.mcmenustestplugin;

import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.parser.tags.*;
import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;

@LookAndFeelAttribute(lookType = MenuLookType.NORMAL, size = 6)
@TitleAttribute(title = "&3Menu of player %player_name%", isPath = false)
public class TestObject
{

    @LinkTag
    @ItemTextureAttribute(value = "arrow")
    @TitleAttribute(title = "Hello, %player_name%", isPath = false)
    private final TestObject2 anotherObject;

    @TitleAttribute(title = "Test checkbox", isPath = false)
    @ItemTextureAttribute(value = "checkbox")
    @LoreAttribute(lore = {"checked: %checkbox_state%"}, isPath = false)
    @CheckBoxTag(checkStateTexturePath = "checked_texture")
    private boolean testCheckButton = false;

    public TestObject(TestObject2 anotherObject) {
        this.anotherObject = anotherObject;
    }

    @ButtonTag
    @TitleAttribute(title = "&aHello there button", isPath = false)
    @ItemTextureAttribute(value = "barrier")
    public void testMethod(Player clicker) {
        clicker.sendMessage("Hello there!");
    }

    public void run() {

    }
}
