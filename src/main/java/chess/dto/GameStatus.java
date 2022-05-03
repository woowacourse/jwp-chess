package chess.dto;

public enum GameStatus {
    READY,
    FINISHED,
    RUNNING;

    public boolean isFinished() {
        return this == FINISHED;
    }

    public boolean isRunning() {
        return this == RUNNING;
    }

    public boolean isReady() {
        return this == READY;
    }
}
