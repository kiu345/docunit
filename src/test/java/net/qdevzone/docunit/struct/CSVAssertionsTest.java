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
            .asText()
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
    void testDocumentContainsText() {
        DocAssertions.assertDoc(filedata)
            .asText()
            .contains("Hello");
    }

}
