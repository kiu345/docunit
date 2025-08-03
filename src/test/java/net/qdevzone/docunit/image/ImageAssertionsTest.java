package net.qdevzone.docunit.image;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import net.qdevzone.docunit.DocAssertions;
import net.qdevzone.docunit.DocumentAssert.FileType;

class ImageAssertionsTest {

    @Test
    void testIsValid() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.jpg"));
        DocAssertions.assertDoc(filedata)
            .asJpeg()
            .isValid();
    }

    @Test
    void testIsJpeg() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.jpg"));
        DocAssertions.assertDoc(filedata)
            .isOfType(FileType.JPG)
            .asJpeg()
            .isJpeg()
            .hasImageSize(1050, 700);
    }

    @Test
    void testIsNotJpeg() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.png"));
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asJpeg()
                .isJpeg();
        });
        Logger.getGlobal().fine(ex.getMessage());

        assertThat(ex.getMessage()).contains("type is not");
    }

    @Test
    void testJpegSizeWrong() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.jpg"));
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asJpeg()
                .hasImageSize(100, 100);
        });
        Logger.getGlobal().fine(ex.getMessage());
    }

    @Test
    void testJpegHasCorrectMeta() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.jpg"));
        DocAssertions.assertDoc(filedata)
            .asJpeg()
            .hasCaption("Testimage")
            .hasKeyword("test");
    }

    @Test
    void testJpegCompare() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.jpg"));
        byte[] referencedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test2.jpg"));

        DocAssertions.assertDoc(filedata)
            .asJpeg()
            .hasAverageRgbDeltaLessThan(referencedata, 3.0);
    }

    @Test
    void testJpegCompareFailDelta() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.jpg"));
        byte[] referencedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test2.jpg"));

        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asJpeg()
                .hasAverageRgbDeltaLessThan(referencedata, 1.0);
        });
        Logger.getGlobal().fine(ex.getMessage());

        assertThat(ex.getMessage()).contains("max delta");
    }

    @Test
    void testJpegCompareFailSize() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.jpg"));
        byte[] referencedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.png"));

        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asJpeg()
                .hasAverageRgbDeltaLessThan(referencedata, 255.0);
        });
        Logger.getGlobal().fine(ex.getMessage());
        assertThat(ex.getMessage()).contains("image sizes do not match");
    }

    @Test
    void testIsGif() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.gif"));
        DocAssertions.assertDoc(filedata)
            .isOfType(FileType.GIF)
            .asGif()
            .isGif()
            .hasImageSize(1900, 1267);
    }

    @Test
    void testIsNotGif() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.png"));
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asGif()
                .isGif();
        });
        Logger.getGlobal().fine(ex.getMessage());

        assertThat(ex.getMessage()).contains("type is not");
    }

    @Test
    void testGifSizeWrong() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.gif"));
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asGif()
                .hasImageSize(100, 100);
        });
        Logger.getGlobal().fine(ex.getMessage());
    }

    @Test
    void testIsPng() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.png"));
        DocAssertions.assertDoc(filedata)
            .isOfType(FileType.PNG)
            .asPng()
            .isPng()
            .hasImageSize(850, 566);
    }

    @Test
    void testIsNotPng() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.gif"));
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asPng()
                .isPng();
        });
        Logger.getGlobal().fine(ex.getMessage());

        assertThat(ex.getMessage()).contains("type is not");
    }

    @Test
    void testPngSizeWrong() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.png"));
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asPng()
                .hasImageSize(100, 100);
        });
        Logger.getGlobal().fine(ex.getMessage());
    }

}
