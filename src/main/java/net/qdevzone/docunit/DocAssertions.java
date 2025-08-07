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

import java.io.IOException;
import java.io.InputStream;

public class DocAssertions {
    protected DocAssertions() {
    }

    public static DocumentAssert assertDoc(InputStream actual) throws IOException {
        byte[] data = actual.readAllBytes();
        return assertDoc(data);
    }

    public static DocumentAssert assertDoc(String actual) {
        return assertDoc(actual.getBytes());
    }

    public static DocumentAssert assertDoc(byte[] actual) {
        return DocumentAssert.forData(actual);
    }
}
