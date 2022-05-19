package ru.armagidon.mcmenusapi.style;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.Validate;
import ru.armagidon.mcmenusapi.misc.MenuAPIConstants;
import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;
import ru.armagidon.mcmenusapi.style.attributes.Title;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuStyleSheet
{

    @Getter HeadStyle headStyle;
    private final Map<String, ElementStyle> objectsStyleSheet = new ConcurrentHashMap<>();

    public MenuStyleSheet() {
        this.headStyle = new HeadStyle(Title.of(""), 1, MenuLookType.NORMAL);
    }

    public ElementStyle getStyle(String id) {
        return objectsStyleSheet.get(id);
    }

    public void setStyle(String id, ElementStyle elementStyle) {
        Validate.notEmpty(id);
        objectsStyleSheet.put(id, elementStyle);
    }


    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class HeadStyle {
        @Getter final Title menuTitle;
        final AtomicInteger menuSize = new AtomicInteger();
        @Getter volatile MenuLookType menuLookType;
        @Getter volatile BiFunction<PlaceholderProcessingContext, String, String> placeHolderProcessor;

        public HeadStyle(Title menuTitle, int menuSize, MenuLookType menuLookType) {
            this.menuTitle = menuTitle;
            this.menuLookType = menuLookType;
            if (menuSize < 1) menuSize = 1;
            else if (menuSize > 6) menuSize = 6;
            if (!getMenuLookType().equals(MenuLookType.NORMAL)) menuSize = 1;
            this.menuSize.set(menuSize);
            this.placeHolderProcessor = MenuAPIConstants.emptyPlaceholderProcessor();
        }

        public synchronized void setMenuLookType(MenuLookType menuLookType) {
            this.menuLookType = menuLookType;
        }

        public void setMenuSize(int newSize) {
            if (!getMenuLookType().equals(MenuLookType.NORMAL)) return;
            if (newSize < 1) newSize = 1;
            else if (newSize > 6) newSize = 6;

            menuSize.set(newSize);
        }

        public synchronized void setPlaceHolderProcessor(BiFunction<PlaceholderProcessingContext, String, String> processor) {
            if (processor == null) return;
            this.placeHolderProcessor = processor;
        }

        public int getMenuSize() {
            return menuSize.get();
        }
    }

}
