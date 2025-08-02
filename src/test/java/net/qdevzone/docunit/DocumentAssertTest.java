package net.qdevzone.docunit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.qdevzone.docunit.DocumentAssert.FileType;

class DocumentAssertTest {

    private static final byte[] TESTBYTES = "Test".getBytes();

    @Test
    void testByteArray() {
        assertThat(DocAssertions.assertDoc(TESTBYTES).actual()).isEqualTo(TESTBYTES);
    }

    @Test
    void testString() {
        assertThat(DocAssertions.assertDoc(new String(TESTBYTES)).actual()).isEqualTo(TESTBYTES);
    }

    @Test
    void testInputStreamByteArray() throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(TESTBYTES);
        assertThat(DocAssertions.assertDoc(is).actual()).isEqualTo(TESTBYTES);
        is.close();
    }

    @Test
    void testEmpty() {
        DocAssertions.assertDoc(new byte[] {}).isEmpty();
    }

    @Test
    void testNotEmpty() {
        DocAssertions.assertDoc(TESTBYTES).isNotEmpty();
    }

    @Test
    void testSize() throws Exception {
        DocAssertions.assertDoc(TESTBYTES).isSize(TESTBYTES.length);
    }

    @Test
    void testSizeFaill() {
        assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(TESTBYTES).isSize(10);
        });
    }

    @Test
    void testIsIn() {
        DocAssertions.assertDoc(TESTBYTES).isIn(TESTBYTES, new byte[] { 1, 2, 3 });
    }

    @Test
    void testIsInFail() {
        assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(TESTBYTES).isIn(new byte[] { 1, 2, 3 });
        });
    }

    @Test
    void testSizeBetween() {
        DocAssertions.assertDoc(TESTBYTES).isSizeBetween(2, TESTBYTES.length + 2);
    }

    @Test
    void testSizeBetweenFail() {
        assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(TESTBYTES).isSizeBetween(0, TESTBYTES.length - 2);
        });
    }

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
