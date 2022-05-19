package ru.armagidon.mcmenusapi.misc.jool;



import net.lollipopmc.common.database.jool.fi.util.function.CheckedBiConsumer;
import net.lollipopmc.common.database.jool.fi.util.function.CheckedConsumer;

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

    public static <T> Consumer<T> checked(CheckedConsumer<T> consumer) {
        return obj -> {
            try {
                consumer.accept(obj);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };
    }

    public static <F, S> BiConsumer<F, S> checked(CheckedBiConsumer<F, S> biConsumer) {
        return (f, s) -> {
            try {
                biConsumer.accept(f, s);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };
    }

}
