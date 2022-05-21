package ru.armagidon.mcmenusapi.misc.jool;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class StreamUtils {

    public static <T> Predicate<T> inversePredicate(Predicate<T> predicate) {
        return (t) -> !predicate.test(t);
    }

    public static <T> Consumer<T> consumerWithCounter(BiConsumer<Integer, T> consumer) {
        AtomicInteger counter = new AtomicInteger(0);
        return item -> consumer.accept(counter.getAndIncrement(), item);
    }

    public static <T> Predicate<T> predicateWithCounter(BiPredicate<Integer, T> predicate) {
        AtomicInteger counter = new AtomicInteger(0);
        return item -> predicate.test(counter.getAndIncrement(), item);
    }

}
