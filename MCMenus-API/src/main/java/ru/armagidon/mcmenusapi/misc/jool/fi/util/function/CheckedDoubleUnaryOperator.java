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
import java.util.function.DoubleUnaryOperator;

/**
 * A {@link DoubleUnaryOperator} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedDoubleUnaryOperator {

    /**
     * Applies this operator to the given operand.
     *
     * @param operand the operand
     * @return the operator result
     */
    double applyAsDouble(double operand) throws Throwable;

    /**
     * @see {@link Sneaky#doubleUnaryOperator(CheckedDoubleUnaryOperator)}
     */
    static DoubleUnaryOperator sneaky(CheckedDoubleUnaryOperator operator) {
        return Sneaky.doubleUnaryOperator(operator);
    }

    /**
     * @see {@link Unchecked#doubleUnaryOperator(CheckedDoubleUnaryOperator)}
     */
    static DoubleUnaryOperator unchecked(CheckedDoubleUnaryOperator operator) {
        return Unchecked.doubleUnaryOperator(operator);
    }

    /**
     * @see {@link Unchecked#doubleUnaryOperator(CheckedDoubleUnaryOperator, Consumer)}
     */
    static DoubleUnaryOperator unchecked(CheckedDoubleUnaryOperator operator, Consumer<Throwable> handler) {
        return Unchecked.doubleUnaryOperator(operator, handler);
    }
}
