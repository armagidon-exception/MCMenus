package ru.armagidon.mcmenusapi.menu.layout;

import com.google.common.collect.ImmutableMap;
import ru.armagidon.mcmenusapi.menu.elements.MenuElement;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AbsoluteLayout implements Layout
{

    private final Map<MenuElement, Position> absolutePositionMap;
    private final Dimension size;

    public AbsoluteLayout(Map<MenuElement, Position> absolutePositionMap, Dimension size) {
        this.absolutePositionMap = ImmutableMap.copyOf(absolutePositionMap);
        this.size = size;
    }

    @Override
    public Map<MenuElement, Position> apply(Set<MenuElement> elements, Position offset, Dimension frameSize) {
        return absolutePositionMap.entrySet().stream().map(e -> Map.entry(e.getKey(), e.getValue().add(offset)))
                .filter(e -> e.getValue().getX() >= 0 && e.getValue().getX() <= Math.min(frameSize.getWidth(), size.getWidth()))
                .filter(e -> e.getValue().getY() >= 0 && e.getValue().getY() <= Math.min(frameSize.getHeight(), size.getHeight()))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Dimension getSize() {
        return size;
    }
}
