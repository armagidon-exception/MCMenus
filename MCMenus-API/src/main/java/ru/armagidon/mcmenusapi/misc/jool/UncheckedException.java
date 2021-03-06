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

/**
 * A generic unchecked exception that wraps checked exceptions thrown from lambdas passed
 * to any of {@link Unchecked}'s methods.
 *
 * @author Lukas Eder
 */
public class UncheckedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UncheckedException(Throwable cause) {
        super(cause);
    }
}
