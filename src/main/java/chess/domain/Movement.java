package chess.domain;

public final class Movement {
    private final String startPoint;
    private final String endPoint;

    public Movement(final String startPoint, final String endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }
}
