package ru.armagidon.mcmenusapi.misc;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RangeConstrainedInt
{
    boolean loop;
    int min;
    int max;
    @NonFinal volatile int currentNumber;

    public RangeConstrainedInt(boolean loop, int min, int max) {
        this(loop, min, max, min);
    }

    public RangeConstrainedInt(boolean loop, int min, int max, int currentNumber) {
        this.loop = loop;
        this.min = min;
        this.max = max;
        this.currentNumber = min;
        setCurrentNumber(currentNumber);
    }

    public synchronized int next() {
        if (currentNumber + 1 >= max && loop)
            currentNumber = 0;
        else if (!loop) return currentNumber;
        return ++currentNumber;
    }

    public synchronized int prior() {
        if (currentNumber == min && loop)
            currentNumber = max;
        else if (!loop) return currentNumber;
        return --currentNumber;
    }

    public int priorPeek() {
        return new RangeConstrainedInt(loop, min, max, currentNumber).prior();
    }

    public int nextPeek() {
        return new RangeConstrainedInt(loop, min, max, currentNumber).next();
    }

    public int get() {
        return currentNumber;
    }

    public void setCurrentNumber(int currentNumber) {
        if (currentNumber < min) currentNumber = min;
        else if (currentNumber > max) currentNumber = max;
        this.currentNumber = currentNumber;
    }
}
