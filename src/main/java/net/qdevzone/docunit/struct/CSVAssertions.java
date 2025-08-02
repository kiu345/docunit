package net.qdevzone.docunit.struct;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import net.qdevzone.docunit.AbstractDocAssert;
import net.qdevzone.docunit.DocumentAssert;

public class CSVAssertions extends AbstractDocAssert<CSVAssertions> {
    class CSVRowAssertions {

        private final CSVRecord row;

        public CSVRowAssertions(CSVRecord row) {
            this.row = row;
        }

        public CSVRowAssertions hasValue(String columnName, String expectedValue) {
            String actual;
            try {
                actual = row.get(columnName);
            }
            catch (IllegalArgumentException ex) {
                throw new AssertionError("row name error: " + ex.getMessage());
            }
            if (!expectedValue.equals(actual)) {
                throw new AssertionError(
                    String.format(
                        "Expected value in column '%s' to be '%s' but was '%s'",
                        columnName, expectedValue, actual
                    )
                );
            }
            return this;
        }

        public CSVRowAssertions hasValueAt(int columnIndex, String expectedValue) {
            String actual = row.get(columnIndex);
            if (!expectedValue.equals(actual)) {
                throw new AssertionError(
                    String.format(
                        "Expected value at column %d to be '%s' but was '%s'",
                        columnIndex, expectedValue, actual
                    )
                );
            }
            return this;
        }

        public CSVAssertions and() {
            return CSVAssertions.this;
        }
    }

    private final String document;
    private final DocumentAssert base;
    private List<CSVRecord> records;
    private List<String> headers;
    private CSVFormat format;
    private Throwable loadError;
    private String delimiter;

    public CSVAssertions(DocumentAssert base, String delimiter, boolean hasHeaders) {
        this(base, delimiter, hasHeaders, StandardCharsets.UTF_8);
    }

    public CSVAssertions(DocumentAssert base, String delimiter, boolean hasHeaders, Charset charSet) {
        super(CSVAssertions.class);
        this.document = (base.actual() != null) ? new String(base.actual()) : null;
        this.base = base;
        this.delimiter = delimiter;
        if (base.actual() == null) {
            return;
        }
        try {
            Reader in = new InputStreamReader(createInputStream(), charSet);
            if (hasHeaders) {
                this.format = CSVFormat.Builder.create()
                    .setIgnoreEmptyLines(true)
                    .setDelimiter(delimiter)
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setAllowMissingColumnNames(true)
                    .get();
            }
            else {
                this.format = CSVFormat.Builder.create()
                    .setIgnoreEmptyLines(true)
                    .setDelimiter(delimiter)
                    .get();
            }
            CSVParser parsed = format.parse(in);
            this.records = new ArrayList<>();
            parsed.forEach(records::add);
            if (hasHeaders) {
                this.headers = parsed.getHeaderNames();
            }
        }
        catch (IOException e) {
            loadError = e;
        }
    }

    public String getDelimiter() {
        return delimiter;
    }

    @Override
    public CSVAssertions isValid() {
        if (loadError != null) {
            throw failure("unloadable document: %s", loadError.getMessage());
        }
        if (document == null) {
            throw failure("document loader returned null");
        }
        return this;
    }

    @Override
    public CSVAssertions isNotValid() {
        if (loadError == null && document != null) {
            throw failure("document was valid");
        }
        return this;
    }

    public CSVAssertions contains(CharSequence... expected) {
        assertThat(document).contains(expected);
        return this;
    }

    public CSVAssertions hasRowCount(int expected) {
        if (records.size() != expected) {
            throw failure("Expected %d rows but found %d", expected, records.size());
        }
        return this;
    }

    public CSVRowAssertions row(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= records.size()) {
            throw failure("Row index %d is out of bounds (0..%d)", rowIndex, records.size() - 1);
        }
        return new CSVRowAssertions(records.get(rowIndex));
    }

    public CSVAssertions hasHeader(String... expectedHeaders) {
        for (String h : expectedHeaders) {
            if (!headers.contains(h)) {
                throw failure("Header '%s' not found in CSV (actual: %s)", h, headers.toString());
            }
        }
        return this;
    }

    @Override
    public byte[] actual() {
        return base.actual();
    }
}
