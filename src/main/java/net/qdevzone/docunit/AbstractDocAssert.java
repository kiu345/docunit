package net.qdevzone.docunit;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.error.MessageFormatter;
import org.assertj.core.internal.Failures;
import org.assertj.core.presentation.Representation;

public abstract class AbstractDocAssert<SELF extends Assert<SELF>> implements Assert<SELF> {
    private static final String ORG_ASSERTJ = "org.assert";
    private static final String NET_QDEVZONE_DOCUNIT = "net.qdevzone.docunit";

    public WritableAssertionInfo info;
    protected final SELF myself;
    static Representation customRepresentation = null;

    @SuppressWarnings("unchecked")
    protected AbstractDocAssert(Class<?> selfType) {
        myself = (SELF) selfType.cast(this);
        info = new WritableAssertionInfo(customRepresentation);
    }

    protected ByteArrayInputStream createInputStream() {
        return new ByteArrayInputStream(actual());
    }

    @Override
    public SELF isEmpty() {
        byte[] data = actual();
        if (!(data == null || data.length == 0)) {
            failure("document not empty");
        }
        return myself;
    }

    @Override
    public SELF isNotEmpty() {
        byte[] data = actual();
        if (data == null || data.length == 0) {
            failure("document not empty");
        }
        return myself;
    }

    @Override
    public SELF isEqualTo(byte[] expected) {
        byte[] data = actual();
        if (expected == null) {
            if (data != null) {
                failure("not equal");
            }
            else {
                return myself;
            }
        }
        if (!expected.equals(data)) {
            failure("not equal");
        }
        return myself;
    }

    @Override
    public SELF isNotEqualTo(byte[] other) {
        byte[] data = actual();
        if (other == null) {
            if (data == null) {
                failure("equal");
            }
            return myself;
        }
        if (other.equals(data)) {
            failure("equal");
        }
        return myself;
    }

    @Override
    public SELF isSize(long expected) {
        byte[] data = actual();
        if (!(data != null && data.length == expected)) {
            failure("size not %d and %d", expected);
        }
        return myself;
    }

    @Override
    public SELF isSizeBetween(long min, long max) {
        byte[] data = actual();
        if (!(data != null && data.length >= min && data.length <= max)) {
            failure("size not between %d and %d", min, max);
        }
        return myself;
    }

    @Override
    public SELF isIn(byte[]... values) {
        return myself;
    }

    @Override
    public SELF isNotIn(byte[]... values) {
        return myself;
    }

    /**
     * Throw an assertion error based on information in this assertion.
     */
    protected void failWithMessage(String errorMessage, Object... arguments) {
        throw failure(errorMessage, arguments);
    }

    protected AssertionError failure(String errorMessage, Object... arguments) {
        AssertionError assertionError = Failures.instance().failureIfErrorMessageIsOverridden(info);
        if (assertionError == null) {
            // error message was not overridden, build it.
            String description = MessageFormatter.instance().format(info.description(), info.representation(), "");
            assertionError = new AssertionError(description + errorMessage.formatted(arguments));
        }
        Failures.instance().removeAssertJRelatedElementsFromStackTraceIfNeeded(assertionError);
        removeCustomAssertRelatedElementsFromStackTraceIfNeeded(assertionError);
        return assertionError;
    }

    private void removeCustomAssertRelatedElementsFromStackTraceIfNeeded(AssertionError assertionError) {
        if (!Failures.instance().isRemoveAssertJRelatedElementsFromStackTrace())
            return;
        if (isAssertjAssertClass())
            return;
        if (isDocuUnitAssertClass())
            return;

        StackTraceElement[] newStackTrace = Arrays.stream(assertionError.getStackTrace())
            .filter(element -> !isElementOfCustomAssert(element))
            .toArray(StackTraceElement[]::new);
        assertionError.setStackTrace(newStackTrace);
    }

    private boolean isAssertjAssertClass() {
        return getClass().getName().startsWith(ORG_ASSERTJ);
    }

    private boolean isDocuUnitAssertClass() {
        return getClass().getName().startsWith(NET_QDEVZONE_DOCUNIT);
    }

    protected boolean isElementOfCustomAssert(StackTraceElement stackTraceElement) {
        Class<?> currentAssertClass = getClass();
        while (currentAssertClass != AbstractAssert.class) {
            if (stackTraceElement.getClassName().equals(currentAssertClass.getName()))
                return true;
            currentAssertClass = currentAssertClass.getSuperclass();
        }
        return false;
    }

}
