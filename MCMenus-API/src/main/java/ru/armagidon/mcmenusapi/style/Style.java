package ru.armagidon.mcmenusapi.style;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.Validate;
import ru.armagidon.mcmenusapi.menu.MenuPanel;

@ToString
public class Style
{
    @Getter private Title title;
    @Getter private Lore lore;
    @Getter @Setter private int slot;

    public Style(Title title, Lore lore) {
        Validate.notNull(title);
        Validate.notNull(lore);
        this.title = title;
        this.lore = lore;
    }

    public void setPosition(int x, int y) {
        this.slot = x + y * MenuPanel.MENU_WIDTH;
    }

    public void setTitle(Title title) {
        Validate.notNull(title);
        this.title = title;
    }

    public void setLore(Lore lore) {
        Validate.notNull(lore);
        this.lore = lore;
    }
}
