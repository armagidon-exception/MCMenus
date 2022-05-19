package ru.armagidon.mcmenusapi.elements;

import lombok.SneakyThrows;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class Button extends MenuElement
{
    private final Method action;

    public Button(String id, Method action) {
        super(id);
        this.action = action;
        if (this.action.getParameterCount() != 1 &&  this.action.getParameterTypes()[0].equals(Player.class))
            throw new IllegalArgumentException("");
    }

    @Override
    @SneakyThrows
    public void handleClickEvent(Object context, Player clicker) {
        action.invoke(context, clicker);
    }
}
