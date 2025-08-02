package net.qdevzone.docunit.archive;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import net.qdevzone.docunit.DocAssertions;
import net.qdevzone.docunit.DocumentAssert.FileType;

class ZipAssertionsTest {

    @Test
    void testFindEntity() throws IOException {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.zip"));
        DocAssertions.assertDoc(filedata)
            .isOfType(FileType.ZIP)
            .asZip()
            .containsElement("test.csv");
    }

    @Test
    void testFindEntityFailed() throws IOException {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.zip"));
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .isOfType(FileType.ZIP)
                .asZip()
                .containsElement("test2.csv");
        });
        Logger.getGlobal().info(ex.getMessage());
    }

    @Test
    void testFindEntityWithSize() throws IOException {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.zip"));
        DocAssertions.assertDoc(filedata)
            .isOfType(FileType.ZIP)
            .asZip()
            .containsElement("test.csv", 284042);
    }

    @Test
    void testFindEntityWithSizeFailed() throws IOException {
        byte[] filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.zip"));
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .isOfType(FileType.ZIP)
                .asZip()
                .containsElement("test.csv", 1000);
        });
        Logger.getGlobal().info(ex.getMessage());
    }

}
