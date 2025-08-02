package net.qdevzone.docunit.image;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import net.qdevzone.docunit.DocAssertions;
import net.qdevzone.docunit.DocumentAssert.FileType;

class ImageAssertionsTest {

    @Test
    void testIsJpeg() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.jpg"));
        DocAssertions.assertDoc(filedata)
            .isOfType(FileType.JPG)
            .asJpeg()
            .hasImageSize(1050, 700);
    }

    @Test
    void testJpegSizeWrong() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.jpg"));
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .isOfType(FileType.JPG)
                .asJpeg()
                .hasImageSize(100, 100);
        });
        Logger.getGlobal().info(ex.getMessage());
    }

    @Test
    void testJpegCompare() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.jpg"));
        byte[] referencedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test2.jpg"));

        DocAssertions.assertDoc(filedata)
            .isOfType(FileType.JPG)
            .asJpeg()
            .hasAverageRgbDeltaLessThan(referencedata, 3.0);
    }

    @Test
    void testJpegCompareFail() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.jpg"));
        byte[] referencedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test2.jpg"));

        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .isOfType(FileType.JPG)
                .asJpeg()
                .hasAverageRgbDeltaLessThan(referencedata, 1.0);
        });
        Logger.getGlobal().info(ex.getMessage());
    }

    @Test
    void testIsGif() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.gif"));
        DocAssertions.assertDoc(filedata)
            .isOfType(FileType.GIF)
            .asJpeg()
            .hasImageSize(1900, 1267);
    }

    @Test
    void testGifSizeWrong() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.gif"));
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .isOfType(FileType.GIF)
                .asJpeg()
                .hasImageSize(100, 100);
        });
        Logger.getGlobal().info(ex.getMessage());
    }

    @Test
    void testIsPng() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.png"));
        DocAssertions.assertDoc(filedata)
            .isOfType(FileType.PNG)
            .asJpeg()
            .hasImageSize(850, 566);
    }

    @Test
    void testPngSizeWrong() throws Exception {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.png"));
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .isOfType(FileType.PNG)
                .asJpeg()
                .hasImageSize(100, 100);
        });
        Logger.getGlobal().info(ex.getMessage());
    }

}
