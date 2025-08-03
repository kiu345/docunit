package net.qdevzone.docunit.archive;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.qdevzone.docunit.AbstractDocAssert;
import net.qdevzone.docunit.DocumentAssert;

public class ZipAssertions extends AbstractDocAssert<ZipAssertions> {
    private byte[] document;
    private final DocumentAssert base;
    private Throwable loadError;

    private List<ZipEntry> entries = new ArrayList<>();

    public ZipAssertions(DocumentAssert base) {
        super(ZipAssertions.class);
        this.document = base.actual();
        this.base = base;
        try {
            loadEntities();
        }
        catch (IOException | NullPointerException e) {
            loadError = e;
        }
    }

    private void loadEntities() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(document);
        ZipInputStream zipStream = new ZipInputStream(is);
        ZipEntry entry;
        while ((entry = zipStream.getNextEntry()) != null) {
            entries.add(entry);
        }
        is.close();
    }

    @Override
    public ZipAssertions isValid() {
        if (loadError != null) {
            throw failure("unloadable document: %s", loadError.getMessage());
        }
        if (document == null) {
            throw failure("document loader returned null");
        }
        return this;
    }

    @Override
    public ZipAssertions isNotValid() {
        if (loadError == null && document != null) {
            throw failure("document was valid");
        }
        return this;
    }

    public ZipAssertions containsElement(String expected) {
        for (var e : entries) {
            if (e.getName().equals(expected)) {
                return this;
            }
        }
        throw failure("entity not found: %s", expected);
    }

    public ZipAssertions containsElement(String expected, long size) {
        for (var e : entries) {
            if (e.getName().equals(expected)) {
                if (e.getSize() == size) {
                    return this;
                }
                throw failure("entity %s found with invalid size: %d but %d expected", expected, e.getSize(), size);
            }
        }
        throw failure("entity not found: %s", expected);
    }

    @Override
    public byte[] actual() {
        return base.actual();
    }
}
