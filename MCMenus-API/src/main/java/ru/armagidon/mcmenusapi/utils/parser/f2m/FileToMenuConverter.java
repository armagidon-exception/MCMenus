package ru.armagidon.mcmenusapi.utils.parser.f2m;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import ru.armagidon.mcmenusapi.elements.MenuModel;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.style.MenuStyleSheet;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Function;


//This is uncompleted feature of this API
public class FileToMenuConverter
{

    private static final ImmutableMap<Class<?>, Function<JsonElement, ?>> mappers = ImmutableMap.<Class<?>, Function<JsonElement, ?>>builder().
            put(MenuModel.class, (el) -> new MenuModel()).
            put(MenuStyleSheet.class, (el) -> new MenuStyleSheet(null))
            .build();

    public MenuPanel convert(JsonFileReader reader) {
        MenuPanel panel = new MenuPanel(reader.getFileName());

        Arrays.stream(panel.getClass().getDeclaredFields()).
                filter(m -> !m.isAnnotationPresent(Exclude.class)).
                filter(f -> (f.getModifiers() & Modifier.STATIC) == 0).forEach(f -> {
                    try {
                        if (f.getType().equals(int.class) || f.getType().equals(Integer.class)) {
                            f.set(panel, reader.getInt(f.getName()));
                        } else if(f.getType().equals(String.class)) {
                            f.set(panel, reader.getString(f.getName()));
                        } else {
                            f.set(panel, reader.getAs(f.getName(), mappers.get(f.getType())));
                        }
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Validation error. Malformed input file");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
        });

        //For each fields and exclude fields with Exclude on it and set parsed values

        return panel; //Return panel
    }
}
