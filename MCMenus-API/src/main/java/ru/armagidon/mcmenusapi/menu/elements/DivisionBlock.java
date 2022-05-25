package ru.armagidon.mcmenusapi.menu.elements;

import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.entity.Player;
import ru.armagidon.mcmenusapi.menu.MenuPanel;
import ru.armagidon.mcmenusapi.menu.container.Container;
import ru.armagidon.mcmenusapi.menu.layout.BlockLayout;
import ru.armagidon.mcmenusapi.menu.layout.Dimension;
import ru.armagidon.mcmenusapi.menu.layout.Layout;
import ru.armagidon.mcmenusapi.menu.layout.Position;
import ru.armagidon.mcmenusapi.style.FrameStyle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DivisionBlock extends MenuElement implements Container
{

    Set<MenuElement> children;
    @NonFinal volatile Layout layout = BlockLayout.create(1, 1);

    public DivisionBlock(String id, MenuElement[] children) {
        super(id);
        this.children = Arrays.stream(children).collect(Collectors.toSet());
    }

    public DivisionBlock(String id, Set<MenuElement> children) {
        super(id);
        this.children = children;
    }

    public DivisionBlock(String id) {
        super(id);
        this.children = new HashSet<>();
    }

    @Override
    public void handleClickEvent(Object context, Player clicker) {}

    @Override
    public void render(Position offset, MenuPanel context) {
        FrameStyle.LookAndFeelProperties properties = context.getStyleSheet().getFrameStyle().getAttribute(FrameStyle.LookAndFeelAttribute.class).get();
        layout.apply(getChildren(), offset, new Dimension(properties.getLookType().getElementsMinimum(), properties.getMenuSize())).forEach((element, position) ->
                element.render(position, context));
    }

    @Override
    public void addElement(MenuElement element) {
        children.add(element);
    }

    @Override
    public MenuElement getElement(String id) {
        Optional<MenuElement> foundElement = getChildren().stream().filter(e -> e.getId().equals(id)).findFirst();
        if (foundElement.isEmpty()) {
            Optional<MenuElement> nestedElement = getChildren().stream()
                    .filter(e -> e instanceof DivisionBlock)
                    .map(e -> (DivisionBlock) e).filter(d -> d.getElement(id) != null)
                    .map(d -> d.getElement(id)).findFirst();
            return nestedElement.orElse(null);
        }
        return foundElement.get();
    }

    @Override
    public Set<MenuElement> getChildren() {
        return ImmutableSet.copyOf(children);
    }

    @Override
    public Layout getLayout() {
        return layout;
    }

    @Override
    public void setLayout(Layout layout) {
        this.layout = layout;
    }
}
