package ru.armagidon.mcmenusapi.menu.elements;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.menu.Renderable;
import ru.armagidon.mcmenusapi.menu.layout.Position;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class MenuElement implements Renderable
{

    @Getter final String id;

    public MenuElement(String id) {
        Validate.notEmpty(id);
        this.id = id;
    }

    public abstract void handleClickEvent(Object context, Player clicker);
    public abstract void render(Position position, MenuPanel context);
}
