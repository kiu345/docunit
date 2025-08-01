package net.qdevzone.docunit;

import net.qdevzone.docunit.doc.WordAssertions;
import net.qdevzone.docunit.excel.ExcelAssertions;
import net.qdevzone.docunit.pdf.PdfAssertions;
import net.qdevzone.docunit.text.TextAssertions;
import net.qdevzone.docunit.xml.XmlAssertions;

public class DocumentAssert extends AbstractDocAssert<DocumentAssert> {

    private byte[] data;

    protected DocumentAssert(byte[] data) {
        super(DocumentAssert.class);
        this.data = data;
    }

    public static DocumentAssert forData(byte[] actual) {
        return new DocumentAssert(actual);
    }

    @Override
    public DocumentAssert isValid() {
        if (data == null) {
            throw failure("null");
        }
        return myself;
    }

    @Override
    public DocumentAssert isNotValid() {
        if (data != null) {
            throw failure("not null");
        }
        return myself;
    }

    public WordAssertions asWord() {
        return new WordAssertions(this);
    }

    public ExcelAssertions asExcel() {
        return new ExcelAssertions(this);
    }

    public PdfAssertions asPdf() {
        return new PdfAssertions(this);
    }

    public TextAssertions asText() {
        return new TextAssertions(this);
    }

    public XmlAssertions asXml() {
        return new XmlAssertions(this);
    }

    @Override
    public byte[] actual() {
        return data;
    }
}
