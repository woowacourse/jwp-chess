package chess.domain;

public enum Camp {
    WHITE(1),
    BLACK(-1),
    NONE(0);

    private final int verticalDirection;

    Camp(int verticalDirection) {
        this.verticalDirection = verticalDirection;
    }

    public int giveVerticalDirectionTo(int distance) {
        return distance * this.verticalDirection;
    }

    @Override
    public String toString() {
        if (this == NONE) {
            return "";
        }
        return name().toLowerCase();
    }
}
