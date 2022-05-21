package ru.armagidon.mcmenusapi.style;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.Validate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuStyleSheet
{

    @Getter FrameStyle frameStyle;
    private final Map<String, ElementStyle> objectsStyleSheet = new ConcurrentHashMap<>();

    public MenuStyleSheet() {
        this.frameStyle = new FrameStyle();
    }

    public ElementStyle getStyle(String id) {
        return objectsStyleSheet.get(id);
    }

    public void setStyle(String id, ElementStyle elementStyle) {
        Validate.notEmpty(id);
        objectsStyleSheet.put(id, elementStyle);
    }
}
