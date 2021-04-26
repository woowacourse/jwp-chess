package chess.controller.spring.params;

public class Page {
    private static final int DEFAULT_PAGE_SIZE = 20;
    private final int offset;
    private final int pageNumber;
    private final int pageSize;

    public Page(int pageNumber) {
        this(pageNumber, DEFAULT_PAGE_SIZE);
    }

    public Page(int pageNumber, int pageSize) {
        this.offset = (pageNumber - 1) * pageSize;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return offset;
    }

    public int getPageSize() {
        return pageSize;
    }
}
