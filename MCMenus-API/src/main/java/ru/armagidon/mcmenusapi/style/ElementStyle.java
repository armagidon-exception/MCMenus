package ru.armagidon.mcmenusapi.style;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import ru.armagidon.mcmenusapi.style.attributes.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ElementStyle extends Style
{

    public ElementStyle() {
        super(Title.of(""),
                Lore.of(),
                TextureAttribute.of(Material.STONE));
    }

}
