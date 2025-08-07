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
package net.qdevzone.docunit.pdf;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.geom.Rectangle2D;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.qdevzone.docunit.DocAssertions;

class PdfAssertionsTest {

    byte[] filedata;

    @BeforeEach
    void setUp() throws Exception {
        filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.pdf"));
    }

    @Test
    void testDocumentLoad() {
        DocAssertions.assertDoc(filedata)
            .asPdf()
            .isValid();
    }

    @Test
    void testDocumentLoadFailNull() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc((byte[]) null)
                .asPdf()
                .isValid();
        });
        Logger.getGlobal().fine(ex.getMessage());
    }

    @Test
    void testDocumentLoadFailInvalid() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc((new byte[] { 0, 1, 2 }))
                .asPdf()
                .isValid();
        });
        Logger.getGlobal().fine(ex.getMessage());

        DocAssertions.assertDoc((new byte[] { 0, 1, 2 }))
            .asPdf()
            .isNotValid();
    }

    @Test
    void testDocumentPagesOK() {
        DocAssertions.assertDoc(filedata)
            .asPdf()
            .hasPages()
            .hasPageCount(1);
    }

    @Test
    void testDocumentPagesFail() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asPdf()
                .hasPages()
                .hasPageCount(2);
        });
        Logger.getGlobal().fine(ex.getMessage());
    }

    @Test
    void testDocumentPagesMinMaxOK() {
        DocAssertions.assertDoc(filedata)
            .asPdf()
            .hasPages()
            .hasPageCount(1, 3);
    }

    @Test
    void testDocumentPageText() {
        DocAssertions.assertDoc(filedata)
            .asPdf()
            .hasTextInPage(1, "Test document");
        DocAssertions.assertDoc(filedata)
            .asPdf()
            .hasTextInPage(1, "Test document", new Rectangle2D.Float(0, 0, 75, 200));
    }

    @Test
    void testDocumentPageTextFail() {
        assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asPdf()
                .hasTextInPage(1, "Nope");
        });
        assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asPdf()
                .hasTextInPage(1, "Test document", new Rectangle2D.Float(0, 0, 20, 200));
        });
    }

}
