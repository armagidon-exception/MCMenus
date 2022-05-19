/**
 * Copyright (c), Data Geekery GmbH, contact@datageekery.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.armagidon.mcmenusapi.misc.jool;

import java.util.Iterator;
import java.util.OptionalLong;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Lukas Eder
 */
class SeqUtils {
    
    static <T> OptionalLong indexOf(Iterator<T> iterator, Predicate<? super T> predicate) {
        for (long index = 0; iterator.hasNext(); index++)
            if (predicate.test(iterator.next()))
                return OptionalLong.of(index);

        return OptionalLong.empty();
    }
    
    /**
     * Sneaky throw any type of Throwable.
     */
    static void sneakyThrow(Throwable throwable) {
        SeqUtils.<RuntimeException>sneakyThrow0(throwable);
    }

    /**
     * Sneaky throw any type of Throwable.
     */
    @SuppressWarnings("unchecked")
    static <E extends Throwable> void sneakyThrow0(Throwable throwable) throws E {
        throw (E) throwable;
    }

    @FunctionalInterface
    interface DelegatingSpliterator<T, U> {
        boolean tryAdvance(Spliterator<? extends T> delegate, Consumer<? super U> action);
    }
    
    static Runnable closeAll(AutoCloseable... closeables) {
        return () -> {
            Throwable t = null;
            
            for (AutoCloseable closeable : closeables) {
                try {
                    closeable.close();
                }
                catch (Throwable t1) {
                    if (t == null)
                        t = t1;
                    else
                        t.addSuppressed(t1);
                }
            }
            
            if (t != null)
                sneakyThrow(t);
        };
    }
}
