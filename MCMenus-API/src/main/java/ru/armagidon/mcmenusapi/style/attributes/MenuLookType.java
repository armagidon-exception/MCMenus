package ru.armagidon.mcmenusapi.style.attributes;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.inventory.InventoryType;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum MenuLookType
{
    NORMAL(InventoryType.CHEST, 9), /*Normal chest inventory*/
    SHORT_ROW(InventoryType.HOPPER, 5); /*Hopper Inventory*/
    //RESULTING_GRID(InventoryType.CRAFTING, 10); /*Crafting Table*/

    InventoryType inventoryType;
    int elementsMinimum;
}
