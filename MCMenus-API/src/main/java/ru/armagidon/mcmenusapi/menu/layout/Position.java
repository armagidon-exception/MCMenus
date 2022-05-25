package ru.armagidon.mcmenusapi.menu.layout;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;

@Setter(onMethod = @__(@Synchronized))
@Getter
@AllArgsConstructor
public class Position
{
    private volatile int x, y;

    public Position() {
        this(0, 0);
    }

    public synchronized void set(Position position) {
        this.x = position.x;
        this.y = position.y;
    }

    public Position add(Position position) {
        return new Position(this.x + position.getX(), this.y + position.getY());
    }

    public int getSlot(MenuLookType lookType) {
        if (lookType.equals(MenuLookType.NORMAL)) {
            return y * lookType.getElementsMinimum() + x;
        } else {
            return x;
        }
    }
}
