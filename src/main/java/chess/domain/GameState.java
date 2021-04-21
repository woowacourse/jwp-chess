package chess.domain;

public enum GameState {
    START,
    END;

    public boolean isEnd() {
        return this == END;
    }

    public boolean isStart() {
        return this == START;
    }
}
