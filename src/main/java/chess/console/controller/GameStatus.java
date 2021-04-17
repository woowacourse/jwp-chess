package chess.console.controller;

public class GameStatus {

    private final boolean isGameEnd;
    public GameStatus(boolean isGameEnd) {
        this.isGameEnd = isGameEnd;
    }

    public boolean isGameEnd() {
        return isGameEnd;
    }
}
