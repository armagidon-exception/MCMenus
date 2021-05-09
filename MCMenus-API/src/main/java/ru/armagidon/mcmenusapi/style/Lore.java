package ru.armagidon.mcmenusapi.style;

import lombok.ToString;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.elements.Renderable;
import ru.armagidon.mcmenusapi.menu.MenuDisplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ToString
public class Lore implements Renderable
{
    private List<String> lore;
    private final StyleObject style;

    private Lore(List<String> lore, StyleObject style) {
        this.lore = lore;
        this.style = style;
    }

    public List<String> getLore() {
        return lore;
    }

    public static Lore of(String... lore) {
        if (MCMenusAPI.isPAPILoaded()) {
            return new Lore(Arrays.asList(lore), new StyleObject.PlaceHolderedStyle());
        } else {
            MCMenusAPI.getInstance().getLogger().severe("PlaceHolderAPI wasn't loaded. Placeholders won't be loaded.");
            return new Lore(Arrays.asList(lore), new StyleObject.StaticStyle());
        }
    }

    @Override
    public void render(MenuDisplay context) {
        lore = lore.stream().map(string -> style.process(string, context)).collect(Collectors.toCollection(ArrayList::new));
    }
}
