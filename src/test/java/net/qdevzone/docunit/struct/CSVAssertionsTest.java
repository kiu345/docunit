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
package net.qdevzone.docunit.struct;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.qdevzone.docunit.DocAssertions;

class CSVAssertionsTest {

    private byte[] filedata;

    @BeforeEach
    void setUp() throws Exception {
        filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.csv"));
    }

    @Test
    void testDocumentLoad() {
        DocAssertions.assertDoc(filedata)
            .asCsv(",")
            .isValid();
    }

    @Test
    void testDocumentLoadFailNull() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc((byte[]) null)
                .asCsv(",")
                .isValid();
        });
        Logger.getGlobal().fine(ex.getMessage());

        DocAssertions.assertDoc((byte[]) null)
            .asCsv(",")
            .isNotValid();
    }

    @Test
    void testDocumentContainsText() {
        DocAssertions.assertDoc(filedata)
            .asCsv(",")
            .contains("First Name");
    }

    @Test
    void testDocumentHasHeader() {
        DocAssertions.assertDoc(filedata)
            .asCsv(",")
            .hasHeader("First Name");
    }

    @Test
    void testDocumentHasHeaderFail() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asCsv(",")
                .hasHeader("No Name");
        });
        Logger.getGlobal().fine(ex.getMessage());
    }

    @Test
    void testDocumentHasRow() {
        DocAssertions.assertDoc(filedata)
            .asCsv(",")
            .row(1)
            .hasValueAt(1, "Mara")
            .hasValueAt(2, "Hashimoto")
            .and()
            .row(3)
            .hasValueAt(1, "Kathleen")
            .hasValueAt(2, "Hanner");
    }

    @Test
    void testDocumentHasRowWithName() {
        DocAssertions.assertDoc(filedata)
            .asCsv(",")
            .row(1)
            .hasValue("First Name", "Mara");
    }

    @Test
    void testDocumentHasRowWithNameFail() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asCsv(",")
                .row(1)
                .hasValue("First Name", "John");
        });
        Logger.getGlobal().fine(ex.getMessage());
    }

    @Test
    void testDocumentHasRowInvalidNameFail() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asCsv(",")
                .row(1)
                .hasValue("Nope", "John");
        });
        Logger.getGlobal().fine(ex.getMessage());
    }

    @Test
    void testDocumentHasRowFail() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asCsv(",")
                .row(4)
                .hasValueAt(1, "Mara")
                .hasValueAt(2, "Hashimoto");
        });
        Logger.getGlobal().fine(ex.getMessage());
    }

    @Test
    void testDocumentHasRows() {
        DocAssertions.assertDoc(filedata)
            .asCsv()
            .hasRowCount(5000);
    }

    @Test
    void testDocumentHasRowsNoHeader() {
        DocAssertions.assertDoc(filedata)
            .asCsv(",", false)
            .hasRowCount(5001);
    }

    @Test
    void testDocumentHasRowsFail() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asCsv()
                .hasRowCount(100);
        });
        Logger.getGlobal().fine(ex.getMessage());
    }

}
