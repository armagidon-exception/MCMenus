package ru.armagidon.mcmenusapi.style;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;


@ToString
public class Style
{
    @Setter @Getter private int x = 0, y = 0;
    @Getter @Setter private Title title;
    @Getter @Setter private Lore lore;

    public Style(Title title, Lore lore) {
        this.title = title;
        this.lore = lore;
    }
}
