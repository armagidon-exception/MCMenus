package ru.armagidon.mcmenusapi.menu.elements;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.misc.MenuAPIConstants;
import ru.armagidon.mcmenusapi.menu.layout.Position;
import ru.armagidon.mcmenusapi.style.ElementStyle;
import ru.armagidon.mcmenusapi.style.FrameStyle;

public abstract class ItemMenuElement extends MenuElement
{

    public ItemMenuElement(String id) {
        super(id);
    }

    @Override
    public void render(Position position, MenuPanel context) {
        ElementStyle elementStyle = context.getStyleSheet().getStyle(getId());
        RenderedElement renderedElement = RenderedElement.create(Material.STONE);
        elementStyle.applyAttributes(renderedElement);
        ItemStack item = renderedElement.asItemStack();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.getPersistentDataContainer().set(MenuAPIConstants.elementTag(), PersistentDataType.STRING, getId());
        item.setItemMeta(meta);

        context.getCanvas().setItem( position.getSlot(context.getStyleSheet()
                .getFrameStyle().getAttribute(FrameStyle.LookAndFeelAttribute.class).get().getLookType()), item);
    }
}
