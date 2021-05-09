package ru.armagidon.mcmenusapi.style;

import lombok.Getter;
import org.apache.commons.lang.Validate;
import ru.armagidon.mcmenusapi.menu.MenuPanel;

import java.util.HashMap;
import java.util.Map;

public class MenuStyleSheet
{
    private final MenuPanel context;

    @Getter private Title title;
    @Getter private int size = 1;

    private final Map<String, Style> objectsStyleSheet = new HashMap<>();

    public MenuStyleSheet(MenuPanel context) {
        this.context = context;
        this.title = Title.of("");
    }

    public Style getStyle(String id) {
        return objectsStyleSheet.get(id);
    }

    public void setStyle(String id, Style style) {
        Validate.notEmpty(id);
        objectsStyleSheet.put(id, style);
    }

    public void setSize(int size) {
        if (size > 0)
            this.size = size;
        context.update();
    }

    public void setTitle(Title title) {
        Validate.notNull(title);
        this.title = title;
        context.update();
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
