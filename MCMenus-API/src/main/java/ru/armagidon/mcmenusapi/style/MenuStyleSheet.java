package ru.armagidon.mcmenusapi.style;

import lombok.Getter;
import org.apache.commons.lang.Validate;
import ru.armagidon.mcmenusapi.MenuPanel;

import java.util.HashMap;
import java.util.Map;

public class MenuStyleSheet
{
    private final MenuPanel context;

    @Getter private String title;
    @Getter private int size;

    private final Map<String, Style> objectsStyleSheet = new HashMap<>();

    public MenuStyleSheet(MenuPanel context) {
        this.context = context;
    }

    public Style getStyle(String id) {
        return objectsStyleSheet.get(id);
    }

    public void setStyle(String id, Style style) {
        Validate.notEmpty(id);
        objectsStyleSheet.put(id, style);
    }

    public void setSize(int size) {
        this.size = size;
        //context.refresh();
    }

    public void setTitle(String title) {
        Validate.notEmpty(title);
        this.title = title;
        //context.refresh();
    }

    @Override
    public String toString() {
        return "MenuStyleSheet{" +
                "title='" + title + '\'' +
                ", size=" + size +
                ", objectsStyleSheet=" + objectsStyleSheet +
                '}';
    }
}
