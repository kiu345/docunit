package net.qdevzone.docunit.doc;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.qdevzone.docunit.DocAssertions;

class WordAssertionsTest {

    byte[] filedata;

    @BeforeEach
    void setUp() throws Exception {
        filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.docx"));
    }

    @Test
    void testDocumentLoad() {
        DocAssertions.assertDoc(filedata)
            .asWord()
            .isValid()
            .hasPages();
    }

    @Test
    void testDocumentLoadFailNull() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc((byte[]) null)
                .asWord()
                .isValid();
        });
        Logger.getGlobal().info(ex.getMessage());

        DocAssertions.assertDoc((byte[]) null)
            .asWord()
            .isNotValid();
    }

    @Test
    void testDocumentPageCount() {
        DocAssertions.assertDoc(filedata)
            .asWord()
            .hasPageCount(1);
    }

    @Test
    void testDocumentPageCountFail() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asWord()
                .hasPageCount(2);
        });
        Logger.getGlobal().info(ex.getMessage());
    }

    @Test
    void testDocumentPageCountBetween() {
        DocAssertions.assertDoc(filedata)
            .asWord()
            .hasPageCount(0, 10);
    }

    @Test
    void testDocumentPageCountBetweenFail() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asWord()
                .hasPageCount(5, 10);
        });
        Logger.getGlobal().info(ex.getMessage());
    }

    @Test
    void testDocumentPageContains() {
        DocAssertions.assertDoc(filedata)
            .asWord()
            .hasContent("Test");
    }

    @Test
    void testDocumentPageContainsFail() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asWord()
                .hasContent("Nope");
        });
        Logger.getGlobal().info(ex.getMessage());
    }

}
