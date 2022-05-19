package ru.armagidon.mcmenustestplugin;

import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.parsers.tags.*;
import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;

@LookAndFeel(lookType = MenuLookType.NORMAL, size = 6)
@TitlePath(title = "&3Menu of player %player_name%")
public class TestObject
{

    @LinkTag
    private final TestObject2 anotherObject;

    public TestObject(TestObject2 anotherObject) {
        this.anotherObject = anotherObject;
    }

    @ButtonTag
    public void testMethod(Player clicker) {
        clicker.sendMessage("Hello there!");
    }
}
