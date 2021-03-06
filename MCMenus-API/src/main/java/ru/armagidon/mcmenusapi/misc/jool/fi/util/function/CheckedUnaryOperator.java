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
package ru.armagidon.mcmenusapi.misc.jool.fi.util.function;

import ru.armagidon.mcmenusapi.misc.jool.Sneaky;
import ru.armagidon.mcmenusapi.misc.jool.Unchecked;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * A {@link UnaryOperator} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedUnaryOperator<T> extends CheckedFunction<T, T> {

    /**
     * @see {@link Sneaky#unaryOperator(CheckedUnaryOperator)}
     */
    static <T> UnaryOperator<T> sneaky(CheckedUnaryOperator<T> operator) {
        return Sneaky.unaryOperator(operator);
    }

    /**
     * @see {@link Unchecked#unaryOperator(CheckedUnaryOperator)}
     */
    static <T> UnaryOperator<T> unchecked(CheckedUnaryOperator<T> operator) {
        return Unchecked.unaryOperator(operator);
    }

    /**
     * @see {@link Unchecked#unaryOperator(CheckedUnaryOperator, Consumer)}
     */
    static <T> UnaryOperator<T> unchecked(CheckedUnaryOperator<T> operator, Consumer<Throwable> handler) {
        return Unchecked.unaryOperator(operator, handler);
    }
}
