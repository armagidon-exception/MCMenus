package ru.armagidon.mcmenusapi.style;

import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.armagidon.mcmenusapi.style.attributes.Attribute;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class Style
{
    Map<Class<? extends Attribute<?>>, Attribute<?>> attributes;
    Set<PreprocessorUnit> preprocessorUnits = new HashSet<>();

    public Style(Attribute<?>... attributes) {
        this.attributes = Stream.of(attributes).map(attr -> Map.entry((Class<? extends Attribute<?>>) attr.getClass(), attr))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
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

    public Set<Attribute<?>> attributes() {
        return ImmutableSet.copyOf(attributes.values());
    }
}
