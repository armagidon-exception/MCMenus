package ru.armagidon.mcmenusapi.elements;

import org.bukkit.inventory.ItemStack;
import ru.armagidon.mcmenusapi.menu.MenuDisplay;

import java.util.function.Consumer;

public class Button extends MenuElement
{

    private final Consumer<MenuDisplay> clickEvent;

    public Button(String id, ItemStack stack, Consumer<MenuDisplay> clickEvent) {
        super(id, stack);
        this.clickEvent = clickEvent;
    }

    @Override
    public void handleClickEvent(MenuDisplay data) {
        clickEvent.accept(data);
    }
}
