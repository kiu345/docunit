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
