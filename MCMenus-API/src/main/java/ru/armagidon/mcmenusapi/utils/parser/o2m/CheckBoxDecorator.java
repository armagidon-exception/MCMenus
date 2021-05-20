package ru.armagidon.mcmenusapi.utils.parser.o2m;

import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;
import ru.armagidon.mcmenusapi.elements.CheckButton;
import ru.armagidon.mcmenusapi.menu.MenuDisplay;

import java.lang.reflect.Field;

class CheckBoxDecorator extends CheckButton
{

    private final Field field;
    private final Object src;

    public CheckBoxDecorator(String id, ItemStack uncheckedState, ItemStack checkedState, boolean checked, Field field, Object src) {
        super(id, uncheckedState, checkedState, checked);
        this.field = field;
        this.src = src;
    }

    @SneakyThrows
    @Override
    public void handleClickEvent(MenuDisplay display) {
        super.handleClickEvent(display);
        field.set(src, isChecked());
    }
}
