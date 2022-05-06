package chess.model.entity;

public class GameEntity {
    private final Long gameId;
    private final String name;
    private final String password;
    private final String turn;

    public GameEntity(Long gameId, String name, String password, String turn) {
        this.gameId = gameId;
        this.name = name;
        this.password = password;
        this.turn = turn;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getName() {
        return name;
    }
}
