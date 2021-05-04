package ru.armagidon.mcmenusapi.style;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class MenuStyleSheet
{
    @Setter @Getter private String title;
    @Setter @Getter private int size;

    private final Map<String, Style> objectsStyleSheet = new HashMap<>();
}
