package chess.entity;

public class GameEntity {
    private static final int TRASH_VALUE = -1;
    private final int gameId;
    private final String turn;

    public GameEntity(String turn) {
        this.gameId = TRASH_VALUE;
        this.turn = turn;
    }

    public GameEntity(int gameId, String turn) {
        this.gameId = gameId;
        this.turn = turn;
    }

    public int getGameId() {
        return gameId;
    }

    public String getTurn() {
        return turn;
    }
}
