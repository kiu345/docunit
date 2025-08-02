package net.qdevzone.docunit.struct;

import static org.assertj.core.api.Assertions.assertThat;

import net.qdevzone.docunit.AbstractDocAssert;
import net.qdevzone.docunit.DocumentAssert;

public class CSVAssertions extends AbstractDocAssert<CSVAssertions> {
    private final String document;
    private final DocumentAssert base;

    public CSVAssertions(DocumentAssert base) {
        super(CSVAssertions.class);
        this.document = new String(base.actual());
        this.base = base;
    }

    @Override
    public CSVAssertions isValid() {
        if (document == null) {
            throw failure("document is null");
        }
        return this;
    }

    @Override
    public CSVAssertions isNotValid() {
        if (document != null) {
            throw failure("document was valid");
        }
        return this;
    }

    public CSVAssertions contains(CharSequence... expected) {
        assertThat(document).contains(expected);
        return this;
    }

    @Override
    public byte[] actual() {
        return base.actual();
    }
}
