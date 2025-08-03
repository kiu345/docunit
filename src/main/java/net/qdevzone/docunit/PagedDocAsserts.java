package net.qdevzone.docunit;

public interface PagedDocAsserts<SELF> {
    public SELF hasPages();

    public SELF hasPageCount(int count);

    public SELF hasPageCount(int min, int max);

}
