package spring.chess.result;

public enum Result {
    WIN,
    DRAW;

    public boolean isDraw() {
        return this == DRAW;
    }
}
