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
        Logger.getGlobal().info(ex.getMessage());

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
        Logger.getGlobal().info(ex.getMessage());
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
        Logger.getGlobal().info(ex.getMessage());
    }

    @Test
    void testDocumentHasRowInvalidNameFail() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asCsv(",")
                .row(1)
                .hasValue("Nope", "John");
        });
        Logger.getGlobal().info(ex.getMessage());
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
        Logger.getGlobal().info(ex.getMessage());
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
        Logger.getGlobal().info(ex.getMessage());
    }

}
