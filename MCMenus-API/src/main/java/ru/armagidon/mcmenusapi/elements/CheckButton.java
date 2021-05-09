package ru.armagidon.mcmenusapi.elements;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import ru.armagidon.mcmenusapi.menu.MenuDisplay;

public class CheckButton extends MenuElement
{

    @Getter private boolean checked;
    private final ItemStack checkedState;
    private final ItemStack uncheckedState;

    public CheckButton(String id, ItemStack uncheckedState, ItemStack checkedState, boolean checked) {
        super(id, checked ? checkedState : uncheckedState);
        this.checked = checked;
        this.checkedState = checkedState;
        this.uncheckedState = uncheckedState;
    }

    @Override
    public void handleClickEvent(MenuDisplay display) {
        setItem((checked = !checked) ? checkedState : uncheckedState);
        callback.accept(this);
        display.refresh(MenuDisplay.RERENDER);
    }

}
