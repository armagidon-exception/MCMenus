package ru.armagidon.mcmenusapi.menu.layout;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

@Setter(onMethod = @__(@Synchronized))
@Getter
@AllArgsConstructor
public class Dimension
{
    private volatile int width, height;

    public Dimension() {
        this(0, 0);
    }

    public synchronized void set(Dimension position) {
        this.width = position.width;
        this.height = position.height;
    }
}
