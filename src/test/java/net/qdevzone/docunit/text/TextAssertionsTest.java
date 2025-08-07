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
package net.qdevzone.docunit.text;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.qdevzone.docunit.DocAssertions;

class TextAssertionsTest {

    private byte[] filedata;

    @BeforeEach
    void setUp() throws Exception {
        filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.txt"));
    }

    @Test
    void testDocumentLoad() {
        DocAssertions.assertDoc(filedata)
            .asText()
            .isValid();
    }

    @Test
    void testDocumentLoadFailNull() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc((byte[]) null)
                .asText()
                .isValid();
        });
        Logger.getGlobal().fine(ex.getMessage());

        DocAssertions.assertDoc((byte[]) null)
            .asText()
            .isNotValid();
    }

    @Test
    void testDocumentContainsText() {
        DocAssertions.assertDoc(filedata)
            .asText()
            .contains("Hello");
    }

}
