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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import net.qdevzone.docunit.AbstractDocAssert;
import net.qdevzone.docunit.DocumentAssert;

public class ExcelAssertions extends AbstractDocAssert<ExcelAssertions> {
    private final Workbook document;
    private final DocumentAssert base;
    private Throwable loadError;

    public ExcelAssertions(DocumentAssert base) {
        super(ExcelAssertions.class);
        Workbook loadadDoc = null;
        try {
            loadadDoc = WorkbookFactory.create(new ByteArrayInputStream(base.actual()));
        }
        catch (IOException | NullPointerException ex) {
            loadError = ex;
        }
        this.document = loadadDoc;
        this.base = base;
    }

    @Override
    public ExcelAssertions isValid() {
        if (loadError != null) {
            throw failure("unloadable document: %s", loadError.getMessage());
        }
        if (document == null) {
            throw failure("document loader returned null");
        }
        return this;
    }

    @Override
    public ExcelAssertions isNotValid() {
        if (loadError == null && document != null) {
            throw failure("document was valid");
        }
        return this;
    }

    public ExcelAssertions hasSheetCount(int expected) {
        if (document.getNumberOfSheets() != expected) {
            throw failure("sheet count %d differs from expected %d", document.getNumberOfSheets(), expected);
        }

        return this;
    }

    public ExcelAssertions hasCellValue(int sheetId, int h, int v, String expected) {
        var sheet = document.getSheetAt(sheetId);
        String actual = sheet.getRow(v).getCell(v).getStringCellValue();

        if (!actual.equals(expected)) {
            throw failure("cell value %s differs from expected %s", actual, expected);
        }

        return this;
    }

    @Override
    public byte[] actual() {
        return base.actual();
    }
}
