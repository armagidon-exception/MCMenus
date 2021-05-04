package ru.armagidon.mcmenusapi.style;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Lore
{
    private final List<String> lore;
    private final StyleObject style;

    private Lore(List<String> lore, StyleObject style) {
        this.lore = lore;
        this.style = style;
    }

    public List<String> getLore() {
        return lore.stream().map(style::process).collect(Collectors.toList());
    }

    public static Lore of(String... lore) {
        return new Lore(Arrays.asList(lore), new StyleObject.StaticStyle());
    }

    public static Lore of(Map<String, String> placeholders, String... lore) {
        return new Lore(Arrays.asList(lore), new StyleObject.PlaceHolderedStyle(placeholders));
    }

}
