package ru.armagidon.mcmenusapi.elements;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.menu.MenuPanel;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Link extends MenuElement
{
    @Setter
    @Getter
    MenuPanel link;
    
    public Link(String id) {
        super(id);
    }

    @Override
    public void handleClickEvent(Object context, Player clicker) {
        if (link == null) return;
        link.show();
    }
}
