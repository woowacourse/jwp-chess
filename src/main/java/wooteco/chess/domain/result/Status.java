package wooteco.chess.domain.result;

public enum Status {
    MOVE,
    FAIL,
    Finish;

    public static Status of(final boolean movable) {
        if (movable) {
            return MOVE;
        }
        return FAIL;
    }

    public boolean isFinish() {
        return this == Finish;
    }

    public boolean isMove() {
        return this == MOVE;
    }
}
