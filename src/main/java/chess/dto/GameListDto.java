package chess.dto;

import java.util.List;

public class GameListDto {
    private final List<Long> gamesId;

    public GameListDto(List<Long> gamesId) {
        this.gamesId = gamesId;
    }

    public static GameListDto from(List<Long> loadGames) {
        return new GameListDto(loadGames);
    }

    public List<Long> getGamesId() {
        return gamesId;
    }
}
