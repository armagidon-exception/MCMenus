package ru.armagidon.mcmenusapi.utils.parser.o2m;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.armagidon.mcmenusapi.MCMenusAPI;
import ru.armagidon.mcmenusapi.elements.Button;
import ru.armagidon.mcmenusapi.menu.MenuDisplay;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.utils.parser.o2m.tags.ButtonTag;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

class ButtonParser implements ElementParser
{

    @Override
    public void parse(Plugin plugin, MenuPanel context, Object src) {
        Method[] methods = src.getClass().getDeclaredMethods();

        Arrays.stream(methods).peek(m -> m.setAccessible(true)).filter(m -> m.isAnnotationPresent(ButtonTag.class))
                .filter(m -> m.getParameterTypes().length == 1 && m.getParameterTypes()[0].equals(MenuDisplay.class)).forEach(m -> {

            ButtonTag buttonData = m.getDeclaredAnnotation(ButtonTag.class);

            ItemStack texture = MenuParser.textureRegistry.get(MenuParser.of(plugin, buttonData.texture()));
            if (texture == null) {
                MCMenusAPI.getInstance().getLogger().severe("Texture " + buttonData.texture() + " for button " + buttonData.id() + " wasn't found in the registry. Skipping.");
                return;
            }

            Button button = new Button(buttonData.id(), texture, (i) -> {
                try {
                    m.invoke(src, i);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
            context.addElement(button);
        });
    }
}
