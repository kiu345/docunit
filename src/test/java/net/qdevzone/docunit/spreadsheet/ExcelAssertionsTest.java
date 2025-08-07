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
package net.qdevzone.docunit.spreadsheet;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.qdevzone.docunit.DocAssertions;

class ExcelAssertionsTest {

    byte[] filedata;

    @BeforeEach
    void setUp() throws Exception {
        filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.xlsx"));
    }

    @Test
    void testDocumentLoad() {
        DocAssertions.assertDoc(filedata)
            .asExcel()
            .isValid();
    }

    @Test
    void testDocumentLoadFailNull() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc((byte[]) null)
                .asExcel()
                .isValid();
        });
        Logger.getGlobal().fine(ex.getMessage());

        DocAssertions.assertDoc((byte[]) null)
            .asExcel()
            .isNotValid();
    }

    @Test
    void testSheetCount() {
        DocAssertions.assertDoc(filedata)
            .asExcel()
            .hasSheetCount(1);
    }

    @Test
    void testSheetCountFail() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asExcel()
                .hasSheetCount(2);
        });
        Logger.getGlobal().fine(ex.getMessage());
    }

    @Test
    void testCellValue() {
        DocAssertions.assertDoc(filedata)
            .asExcel()
            .hasCellValue(0, 0, 0, "Test");
    }

    @Test
    void testCellValueFail() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asExcel()
                .hasCellValue(0, 0, 0, "Nope");
        });
        Logger.getGlobal().fine(ex.getMessage());
    }

}
