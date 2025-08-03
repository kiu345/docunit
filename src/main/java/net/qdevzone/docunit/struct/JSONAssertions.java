package net.qdevzone.docunit.struct;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.assertj.core.util.Lists;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import net.qdevzone.docunit.AbstractDocAssert;
import net.qdevzone.docunit.DocumentAssert;

public class JSONAssertions extends AbstractDocAssert<JSONAssertions> {
    private final String document;
    private final DocumentAssert base;

    public JSONAssertions(DocumentAssert base) {
        super(JSONAssertions.class);
        this.document = (base.actual() != null) ? new String(base.actual()) : null;
        this.base = base;
    }

    @Override
    public JSONAssertions isValid() {
        if (document == null) {
            throw failure("document is null");
        }
        JsonPath.parse(createInputStream());
        try {
            new ObjectMapper().readTree(base.actual());
        }
        catch (IOException e) {
            throw failureWithCause(e, "Input is not valid JSON: %s", e.getMessage());
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

    public JSONAssertions hasKey(String jsonPath) {
        try {
            Object value = JsonPath.read(createInputStream(), jsonPath);
            if (value == null) {
                throw failure("JsonPath '%s' does not exist or is null", jsonPath);
            }
        }
        catch (PathNotFoundException e) {
            throw failureWithCause(e, "JsonPath '%s' not found", jsonPath);
        }
        catch (IOException e) {
            throw failureWithCause(e, e.getMessage());
        }
        return this;
    }

    private static final Class<?>[] PLAIN_VALUE_CLASSES = { String.class, Integer.class, Long.class, Boolean.class };

    private boolean isPlainValue(Class<?> type) {
        return Lists.list(PLAIN_VALUE_CLASSES).contains(type);
    }

    public JSONAssertions hasValue(String jsonPath, Object expected) {
        try {
            Object actual = JsonPath.read(createInputStream(), jsonPath);
            if (expected != null && !isPlainValue(expected.getClass())) {
                // convert to json only data
                String expectedJson = new ObjectMapper().writeValueAsString(expected);
                expected = JsonPath.read(expectedJson, "$");
            }
            if (!Objects.equals(actual, expected)) {
                throw failure(
                    "JsonPath '%s' expected <%s>, but was <%s>",
                    jsonPath, expected, actual
                );
            }
        }
        catch (PathNotFoundException e) {
            throw failure("JsonPath '%s' not found: %s", jsonPath, e.getMessage());
        }
        catch (IOException e) {
            throw failure(e.getMessage());
        }
        return this;
    }

    public JSONAssertions hasArraySize(String jsonPath, int expectedSize) {
        try {
            List<?> array = JsonPath.read(createInputStream(), jsonPath);
            if (array.size() != expectedSize) {
                throw failure(
                    "JsonPath '%s' has %d elements, expected: %d",
                    jsonPath, array.size(), expectedSize
                );
            }
        }
        catch (ClassCastException e) {
            throw failure("JsonPath '%s' is not an arary", jsonPath);
        }
        catch (PathNotFoundException e) {
            throw failureWithCause(e, "JsonPath '%s' not found", jsonPath);
        }
        catch (IOException e) {
            throw failureWithCause(e, e.getMessage());
        }
        return this;
    }

    @Override
    public byte[] actual() {
        return base.actual();
    }
}
