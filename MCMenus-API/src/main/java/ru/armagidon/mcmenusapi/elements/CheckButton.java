package ru.armagidon.mcmenusapi.elements;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.armagidon.mcmenusapi.menu.MenuDisplay;

import java.util.function.Consumer;

public class CheckButton extends MenuElement
{

    private boolean checked;
    private final ItemStack checkedState;
    private final ItemStack uncheckedState;

    private final Consumer<Boolean> callBack;

    public CheckButton(String id, ItemStack uncheckedState, ItemStack checkedState, boolean checked) {
        this(id, uncheckedState, checkedState, checked, (s) -> {});
    }

    public CheckButton(String id, ItemStack uncheckedState, ItemStack checkedState, boolean checked, Consumer<Boolean> callBack) {
        super(id, checked ? checkedState : uncheckedState);
        this.checked = checked;
        this.checkedState = checkedState;
        this.uncheckedState = uncheckedState;
        this.callBack = callBack;
    }

    @Override
    public void handleClickEvent(InventoryClickEvent data) {
        MenuDisplay display = (MenuDisplay) data.getInventory().getHolder();
        setItem((checked = !checked) ? checkedState : uncheckedState);
        callBack.accept(checked);
        display.refresh(MenuDisplay.RERENDER);
    }
}
