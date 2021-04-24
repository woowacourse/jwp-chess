package chess.domain.chess;

public enum Status {
    STOP, RUNNING, KING_DEAD, TERMINATED;

    public boolean isRunning() {
        return this == RUNNING;
    }

    public boolean isTerminated() {
        return this == TERMINATED;
    }
}
