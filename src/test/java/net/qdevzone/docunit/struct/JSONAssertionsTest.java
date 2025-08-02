package net.qdevzone.docunit.struct;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.qdevzone.docunit.DocAssertions;

class JSONAssertionsTest {

    public class Meta {
        private String name;
        private String version;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

    }

    private byte[] filedata;

    @BeforeEach
    void setUp() throws Exception {
        filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.json"));
    }

    @Test
    void testDocumentLoad() {
        DocAssertions.assertDoc(filedata)
            .asJson()
            .isValid();
    }

    @Test
    void testDocumentLoadFailNull() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc((byte[]) null)
                .asJson()
                .isValid();
        });
        Logger.getGlobal().info(ex.getMessage());
    }

    @Test
    void testDocumentContainsText() {
        DocAssertions.assertDoc(filedata)
            .asJson()
            .contains("countries");
    }

    @Test
    void testDocumentContainsKey() {
        DocAssertions.assertDoc(filedata)
            .asJson()
            .hasKey("$.countries[4].name");
    }

    @Test
    void testDocumentContainsStringValue() {
        DocAssertions.assertDoc(filedata)
            .asJson()
            .hasValue("$.countries[4].name", "Andorra");
    }

    @Test
    void testDocumentContainsObjectValue() {
        Meta test = new Meta();
        test.setName("CountryInfo");
        test.setVersion("1.0");

        DocAssertions.assertDoc(filedata)
            .asJson()
            .hasValue("$.meta", test);
    }

    @Test
    void testDocumentArrayCount() {
        DocAssertions.assertDoc(filedata)
            .asJson()
            .hasArraySize("$.countries", 246);
    }

}
