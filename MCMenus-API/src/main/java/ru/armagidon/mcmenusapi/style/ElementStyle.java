package ru.armagidon.mcmenusapi.style;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import ru.armagidon.mcmenusapi.style.attributes.Attribute;
import ru.armagidon.mcmenusapi.style.attributes.Lore;
import ru.armagidon.mcmenusapi.style.attributes.TextureAttribute;
import ru.armagidon.mcmenusapi.style.attributes.Title;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ElementStyle
{
    Map<Class<? extends Attribute<?>>, Attribute<?>> attributes;
    Set<PreprocessorUnit> preprocessorUnits = new HashSet<>();


    public ElementStyle(Attribute<?>... attributes) {
        this.attributes = Stream.of(attributes).map(attr -> Map.entry((Class<? extends Attribute<?>>) attr.getClass(), attr))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public ElementStyle() {
        this(Title.of(""), Lore.of(), TextureAttribute.of(Material.STONE));
    }

    public <T> Attribute<T> getAttribute(Class<? extends Attribute<T>> attributeClass) {
        return (Attribute<T>) attributes.get(attributeClass);
    }

    public String preprocess(String input) {
        String output = input;
        for (PreprocessorUnit preprocessorUnit : preprocessorUnits) {
            output = preprocessorUnit.process(output);
        }
        return output;
    }

    public void addPreprocessorUnit(PreprocessorUnit preprocessorUnit) {
        preprocessorUnits.add(preprocessorUnit);
    }

}
