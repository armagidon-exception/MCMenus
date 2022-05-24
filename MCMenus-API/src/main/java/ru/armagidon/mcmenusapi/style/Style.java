package ru.armagidon.mcmenusapi.style;

import com.google.common.collect.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.ArrayUtils;
import org.w3c.dom.Attr;
import ru.armagidon.mcmenusapi.menu.Renderable;
import ru.armagidon.mcmenusapi.style.attributes.Attribute;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class Style
{
    ClassToInstanceMap<Attribute<?>> attributes;
    Set<PreprocessorUnit> preprocessorUnits = new HashSet<>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Style(Attribute... attributes) {
        this.attributes = ImmutableClassToInstanceMap
                .copyOf(Arrays.stream(attributes)
                        .collect(Collectors.toMap((Function<Attribute, ? extends Class<? extends Attribute>>) Attribute::getClass, Function.identity())));
    }

    public <T> Attribute<T> getAttribute(Class<? extends Attribute<T>> attributeClass) {
        return attributes.getInstance(attributeClass);
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
        return ImmutableSet.copyOf(this.attributes.values());
    }

    public void applyAttributes(Renderable input) {
        attributes().forEach(attribute -> attribute.display(this::preprocess, input));
    }
}
