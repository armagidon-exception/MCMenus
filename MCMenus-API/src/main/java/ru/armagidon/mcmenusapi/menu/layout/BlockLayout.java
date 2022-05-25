package ru.armagidon.mcmenusapi.menu.layout;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.armagidon.mcmenusapi.menu.elements.MenuElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlockLayout implements Layout
{

    int width, height;

    @Override
    public Map<MenuElement, Position> apply(Set<MenuElement> elements, Position offset, Dimension frameSize) {
        List<MenuElement> elementList = new ArrayList<>(elements);
        System.out.println(elementList.size());
        ImmutableMap.Builder<MenuElement, Position> outputBuilder = ImmutableMap.builder();
        for (int y = offset.getY(); y < Math.min(frameSize.getHeight(), this.height + offset.getY()); y++) {
            for (int x = offset.getX(); x < Math.min(frameSize.getWidth(), this.width + offset.getX()); x++) {
                int index = (y - offset.getY()) * frameSize.getHeight() + (x - offset.getX()) ;
                if (index >= elementList.size()) return outputBuilder.build();
                MenuElement element = elementList.get(index);
                Position position = new Position(x, y);
                outputBuilder.put(element, position);
            }
        }
        return outputBuilder.build();
    }

    @Override
    public Dimension getSize() {
        return new Dimension(width, height);
    }

    public static BlockLayout create(int width, int height) {
        return new BlockLayout(width, height);
    }

}
