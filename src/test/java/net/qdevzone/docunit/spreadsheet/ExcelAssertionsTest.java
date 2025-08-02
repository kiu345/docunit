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
        Logger.getGlobal().info(ex.getMessage());

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
        Logger.getGlobal().info(ex.getMessage());
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
        Logger.getGlobal().info(ex.getMessage());
    }

}
