package ru.armagidon.mcmenusapi.menuelements;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.armagidon.mcmenusapi.menu.MenuDisplay;

public class CheckButton extends MenuElement
{

    private boolean checked;
    private final ItemStack checkedState;
    private final ItemStack uncheckedState;

    public CheckButton(String id, ItemStack stack, ItemStack checkedState, boolean checked) {
        super(id, checked ? checkedState : stack);
        this.checkedState = checkedState;
        this.uncheckedState = stack.clone();
        this.checked = checked;
    }

    @Override
    public void onClick(InventoryClickEvent data) {
        MenuDisplay display = (MenuDisplay) data.getInventory().getHolder();
        setItem((checked = !checked) ? checkedState : uncheckedState);
        display.refresh(true);
    }
}
