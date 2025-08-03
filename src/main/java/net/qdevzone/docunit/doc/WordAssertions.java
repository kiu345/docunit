package net.qdevzone.docunit.doc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import net.qdevzone.docunit.AbstractDocAssert;
import net.qdevzone.docunit.DocumentAssert;
import net.qdevzone.docunit.PagedDocAsserts;

public class WordAssertions extends AbstractDocAssert<WordAssertions> implements PagedDocAsserts<WordAssertions> {
    private final XWPFDocument document;
    private final DocumentAssert base;
    private Throwable loadError;

    public WordAssertions(DocumentAssert base) {
        super(WordAssertions.class);
        XWPFDocument loadadDoc = null;
        try {
            loadadDoc = new XWPFDocument(new ByteArrayInputStream(base.actual()));
        }
        catch (IOException | NullPointerException ex) {
            loadError = ex;
        }
        this.document = loadadDoc;
        this.base = base;
    }

    @Override
    public WordAssertions isValid() {
        if (loadError != null) {
            throw failure("unloadable document: %s", loadError.getMessage());
        }
        if (document == null) {
            throw failure("document loader returned null");
        }
        return this;
    }

    @Override
    public WordAssertions isNotValid() {
        if (loadError == null && document != null) {
            throw failure("document was valid");
        }
        return this;
    }

    @Override
    public byte[] actual() {
        return base.actual();
    }

    private int pageCount() {
        try {
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            int pageCount = extractor.getExtendedProperties().getPages();
            extractor.close();
            return pageCount;
        }
        catch (IOException e) {
            Logger.getGlobal().warning(e.getMessage());
            return -1;
        }
    }

    @Override
    public WordAssertions hasPages() {
        if (pageCount() == 0) {
            throw failure("no pages found");
        }
        return this;
    }

    @Override
    public WordAssertions hasPageCount(int count) {
        int actualPages = pageCount();
        if (actualPages != count) {
            throw failure("page count %d differs from expected %d", actualPages, count);
        }
        return this;
    }

    @Override
    public WordAssertions hasPageCount(int min, int max) {
        int actualPages = pageCount();
        if (actualPages < min || actualPages > max) {
            throw failure("page count not in range %d >[ %d ]> %d", min, actualPages, max);
        }
        return this;
    }

    public WordAssertions hasContent(String expected) {
        List<IBodyElement> elements = document.getBodyElements();
        for (var e : elements) {
            if (e instanceof XWPFParagraph para) {
                if (para.getText().contains(expected)) {
                    return this;
                }
            }
            if (e instanceof XWPFTable table) {
                if (table.getText().contains(expected)) {
                    return this;
                }
            }
        }

        throw failure("text not found: %s", expected);
    }
}
