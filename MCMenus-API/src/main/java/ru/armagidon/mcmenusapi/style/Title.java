package ru.armagidon.mcmenusapi.style;

import java.util.Map;

public class Title
{
    private final String title;
    private final StyleObject style;

    private Title(String title, StyleObject style) {
        this.title = title;
        this.style = style;
    }

    public String getTitle() {
        return style.process(title);
    }

    public static Title of(String input) {
        return new Title(input, new StyleObject.StaticStyle());
    }

    public static Title of(String input, Map<String, String> placeholders) {
        return new Title(input, new StyleObject.PlaceHolderedStyle(placeholders));
    }
}
