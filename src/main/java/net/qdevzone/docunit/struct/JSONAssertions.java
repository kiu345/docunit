package net.qdevzone.docunit.struct;

import static org.assertj.core.api.Assertions.assertThat;

import net.qdevzone.docunit.AbstractDocAssert;
import net.qdevzone.docunit.DocumentAssert;

public class JSONAssertions extends AbstractDocAssert<JSONAssertions> {
    private final String document;
    private final DocumentAssert base;

    public JSONAssertions(DocumentAssert base) {
        super(JSONAssertions.class);
        this.document = new String(base.actual());
        this.base = base;
    }

    @Override
    public JSONAssertions isValid() {
        if (document == null) {
            throw failure("document is null");
        }
        return this;
    }

    @Override
    public JSONAssertions isNotValid() {
        if (document != null) {
            throw failure("document was valid");
        }
        return this;
    }

    public JSONAssertions contains(CharSequence... expected) {
        assertThat(document).contains(expected);
        return this;
    }

    @Override
    public byte[] actual() {
        return base.actual();
    }
}
