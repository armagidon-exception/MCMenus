package ru.armagidon.mcmenustestplugin;

import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.parser.tags.*;
import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;

@LookAndFeel(lookType = MenuLookType.NORMAL, size = 6)
@TitlePath(title = "&3Menu of player %player_name%", isPath = false)
public class TestObject
{

    @LinkTag
    @TitlePath(title = "Hello, %player_name%", isPath = false)
    @ItemTexturePath(path = "arrow")
    private final TestObject2 anotherObject;

    public TestObject(TestObject2 anotherObject) {
        this.anotherObject = anotherObject;
    }

    @ButtonTag
    @TitlePath(title = "&aHello there button", isPath = false)
    @ItemTexturePath(path = "barrier")
    public void testMethod(Player clicker) {
        clicker.sendMessage("Hello there!");
    }
}
