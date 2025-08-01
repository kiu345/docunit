package net.qdevzone.docunit;

public interface Assert<SELF extends Assert<SELF>> {
    SELF isEqualTo(byte[] expected);

    SELF isNotEqualTo(byte[] other);

    SELF isEmpty();

    SELF isNotEmpty();

    SELF isValid();

    SELF isNotValid();

    SELF isSize(long expected);

    SELF isSizeBetween(long min, long max);

    SELF isIn(byte[]... values);

    SELF isNotIn(byte[]... values);

    /**
     * Returns actual (the data currently under test).
     */
    byte[] actual();

}
