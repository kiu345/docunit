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
        Logger.getGlobal().info(ex.getMessage());
    }

    @Test
    void testDocumentLoadFailEmpty() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc((new byte[] { 0, 1, 2 }))
                .asPdf()
                .isValid();
        });
        Logger.getGlobal().info(ex.getMessage());
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
        Logger.getGlobal().info(ex.getMessage());
    }
    @Test
    void testDocumentPagesMinMaxOK() {
        DocAssertions.assertDoc(filedata)
            .asPdf()
            .hasPages()
            .hasPageCount(1,3);
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


}
