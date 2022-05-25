package ru.armagidon.mcmenusapi.menu.elements;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.menu.MenuPanel;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Link extends ItemMenuElement
{
    @Getter
    MenuPanel link;
    
    public Link(String id, MenuPanel link) {
        super(id);
        this.link = link;
    }

    @Override
    public void handleClickEvent(Object context, Player clicker) {
        link.show();
    }
}
