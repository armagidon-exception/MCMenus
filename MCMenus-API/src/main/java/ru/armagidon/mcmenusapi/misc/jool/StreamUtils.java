package ru.armagidon.mcmenusapi.misc.jool;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class StreamUtils {

    public static <T> Consumer<T> withCounter(BiConsumer<Integer, T> consumer) {
        AtomicInteger counter = new AtomicInteger(0);
        return item -> consumer.accept(counter.getAndIncrement(), item);
    }

    public static <T> Predicate<T> withCounter(BiPredicate<Integer, T> predicate) {
        AtomicInteger counter = new AtomicInteger(0);
        return item -> predicate.test(counter.getAndIncrement(), item);
    }

}
