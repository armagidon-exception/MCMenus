package ru.armagidon.mcmenusapi.elements;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class Button extends MenuElement
{

    private final Consumer<InventoryClickEvent> clickEvent;

    public Button(String id, ItemStack stack, Consumer<InventoryClickEvent> clickEvent) {
        super(id, stack);
        this.clickEvent = clickEvent;
    }

    @Override
    public void handleClickEvent(InventoryClickEvent data) {
        clickEvent.accept(data);
    }
}
