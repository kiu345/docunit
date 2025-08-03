package net.qdevzone.docunit;

import java.io.IOException;
import java.io.InputStream;

public class DocAssertions {
    protected DocAssertions() {
    }

    public static DocumentAssert assertDoc(InputStream actual) throws IOException {
        byte[] data = actual.readAllBytes();
        return assertDoc(data);
    }

    public static DocumentAssert assertDoc(String actual) {
        return assertDoc(actual.getBytes());
    }

    public static DocumentAssert assertDoc(byte[] actual) {
        return DocumentAssert.forData(actual);
    }
}
