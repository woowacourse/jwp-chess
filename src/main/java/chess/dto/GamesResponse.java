package chess.dto;

import chess.entity.GameEntity;
import java.util.List;

public class GamesResponse {

    List<GameEntity> games;
    GameCountDto gameCount;

    public GamesResponse(GameCountDto gameCount, List<GameEntity> games) {
        this.games = games;
        this.gameCount = gameCount;
    }

    public List<GameEntity> getGames() {
        return games;
    }

    public GameCountDto getGameCount() {
        return gameCount;
    }

    @Override
    public String toString() {
        return "GamesResponse{" +
                "games=" + games +
                ", gameCountDto=" + gameCount +
                '}';
    }
}
