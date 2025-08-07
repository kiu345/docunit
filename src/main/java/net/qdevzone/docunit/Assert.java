/*
 * Copyright (c) 2025-2025 the original author or authors
 *
 * See the README file(s) distributed with this work for additional information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package net.qdevzone.docunit;

public interface Assert<SELF extends Assert<SELF>> {
    SELF isEqualTo(byte[] expected);

    SELF isNotEqualTo(byte[] other);

    SELF isEmpty();

    SELF isNotEmpty();

    SELF isValid();

    SELF isNotValid();

    SELF isSize(long expected);

    SELF isSizeBetween(long min, long max);

    SELF isIn(byte[]... values);

    SELF isNotIn(byte[]... values);

    /**
     * Returns actual (the data currently under test).
     */
    byte[] actual();

}
