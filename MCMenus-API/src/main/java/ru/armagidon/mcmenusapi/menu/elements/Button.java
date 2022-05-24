package ru.armagidon.mcmenusapi.menu.elements;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class Button extends MenuElement
{

    private final Consumer<Button> action;

    public Button(String id, Consumer<Button> action) {
        super(id);
        this.action = action;
    }

    @Override
    public void handleClickEvent(Object context, Player clicker) {
        action.accept(this);
    }
}
