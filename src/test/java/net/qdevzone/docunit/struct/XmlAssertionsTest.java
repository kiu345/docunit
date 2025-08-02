package net.qdevzone.docunit.struct;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.qdevzone.docunit.DocAssertions;

class XmlAssertionsTest {

    byte[] filedata;

    @BeforeEach
    void setUp() throws Exception {
        filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.xml"));
    }

    @Test
    void testDocumentLoad() {
        DocAssertions.assertDoc(filedata)
            .asXml()
            .isValid();
    }

    @Test
    void testDocumentLoadFailNull() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc((byte[]) null)
                .asXml()
                .isValid();
        });
        Logger.getGlobal().info(ex.getMessage());
    }

    @Test
    void testDocumentLoadFailEmpty() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc((new byte[] { 0, 1, 2 }))
                .asXml()
                .isValid();
        });
        Logger.getGlobal().info(ex.getMessage());
    }

    @Test
    void testDocumentRoot() {
        DocAssertions.assertDoc(filedata)
            .asXml()
            .hasRootElement("data");
    }

    @Test
    void testDocumentRootFail() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asXml()
                .hasRootElement("nope");
        });
        Logger.getGlobal().info(ex.getMessage());
    }

}
