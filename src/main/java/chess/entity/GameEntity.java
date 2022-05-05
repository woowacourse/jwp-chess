package chess.entity;

public class GameEntity {
    private static final int TRASH_INT = -1;
    private static final String TRASH_STRING = "";

    private final int gameId;
    private final String turn;

    private GameEntity(int gameId, String turn) {
        this.gameId = gameId;
        this.turn = turn;
    }

    public static GameEntity of(int gameId) {
        return new GameEntity(gameId, TRASH_STRING);
    }

    public static GameEntity of(String turn) {
        return new GameEntity(TRASH_INT, turn);
    }

    public static GameEntity of(int gameId, String turn) {
        return new GameEntity(gameId, turn);
    }

    public int getGameId() {
        return gameId;
    }

    public String getTurn() {
        return turn;
    }
}
