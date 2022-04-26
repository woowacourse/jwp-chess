package chess.dto;

import java.util.List;

public class GamesResponse {

    List<GameInfoDto> games;
    GameCountDto gameCount;

    public GamesResponse(GameCountDto gameCount, List<GameInfoDto> games) {
        this.games = games;
        this.gameCount = gameCount;
    }

    public List<GameInfoDto> getGames() {
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
