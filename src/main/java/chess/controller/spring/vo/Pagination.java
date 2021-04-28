package chess.controller.spring.vo;

public class Pagination {
    private static final int CONTENT_COUNTS_PER_PAGE = 5;

    private final int offset;
    private final int pageCounts;

    public Pagination(int currentPageIndex, int totalContentCounts) {
        this.offset = (currentPageIndex - 1) * CONTENT_COUNTS_PER_PAGE;
        this.pageCounts = (int) Math.ceil(((double) totalContentCounts) / CONTENT_COUNTS_PER_PAGE);
    }

    public int getOffset() {
        return offset;
    }

    public int getContentCountsPerPage() {
        return CONTENT_COUNTS_PER_PAGE;
    }

    public int getPageCounts() {
        return pageCounts;
    }
}
