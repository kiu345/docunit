package net.qdevzone.docunit;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.qdevzone.docunit.DocumentAssert.FileType;

class DocumentAssertTest {

    @Test
    void testIsOfTypePng() throws Exception {
        Path testFile = Path.of("src", "test", "resources", "files", "test.png");
        byte[] filedata = Files.readAllBytes(testFile);

        DocAssertions.assertDoc(filedata).isValid().isOfType(FileType.PNG);

        assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata).isOfType(FileType.WORD);
        });
    }

    @Test
    void testIsOfTypeJpg() throws Exception {
        Path testFile = Path.of("src", "test", "resources", "files", "test.jpg");
        byte[] filedata = Files.readAllBytes(testFile);

        DocAssertions.assertDoc(filedata).isValid().isOfType(FileType.JPG);

        assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata).isOfType(FileType.WORD);
        });
    }

    @Test
    void testIsOfTypeGif() throws Exception {
        Path testFile = Path.of("src", "test", "resources", "files", "test.gif");
        byte[] filedata = Files.readAllBytes(testFile);

        DocAssertions.assertDoc(filedata).isValid().isOfType(FileType.GIF);

        assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata).isOfType(FileType.WORD);
        });
    }

    @Test
    void testIsOfTypePdf() throws Exception {
        Path testFile = Path.of("src", "test", "resources", "files", "test.pdf");
        byte[] filedata = Files.readAllBytes(testFile);

        DocAssertions.assertDoc(filedata).isValid().isOfType(FileType.PDF);

        assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata).isOfType(FileType.WORD);
        });
    }

}
