package ru.armagidon.mcmenusapi.style;

import lombok.ToString;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.elements.Renderable;
import ru.armagidon.mcmenusapi.menu.MenuDisplay;

@ToString
public class Title implements Renderable
{
    private String title;
    private final StyleObject style;

    private Title(String title, StyleObject style) {
        this.title = title;
        this.style = style;
    }

    public String getTitle() {
        return title;
    }

    public static Title of(String input) {
        if (MCMenusAPI.isPAPILoaded()) {
            return new Title(input, new StyleObject.PlaceHolderedStyle());
        } else {
            MCMenusAPI.getInstance().getLogger().severe("PlaceHolderAPI wasn't loaded. Placeholders won't be loaded.");
            return new Title(input, new StyleObject.StaticStyle());
        }
    }

    @Override
    public void render(MenuDisplay context) {
        title = style.process(title, context);
    }
}
