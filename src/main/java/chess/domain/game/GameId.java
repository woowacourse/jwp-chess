package chess.domain.game;

import java.util.UUID;

public class GameId {
    private final String gameId;

    private GameId(String gameId) {
        this.gameId = gameId;
    }

    public static GameId from(String gameId) {
        return new GameId(gameId);
    }

    public static GameId random() {
        String uuid = UUID.randomUUID().toString();
        return new GameId(uuid);
    }

    public String getGameId() {
        return gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GameId gameId1 = (GameId) o;

        return gameId.equals(gameId1.gameId);
    }

    @Override
    public int hashCode() {
        return gameId.hashCode();
    }

    @Override
    public String toString() {
        return "GameId{" +
                "gameId='" + gameId + '\'' +
                '}';
    }
}
