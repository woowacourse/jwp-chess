package chess.dto;

import java.util.List;

public class GameListDto {
    private final List<Integer> gamesId;

    public GameListDto(List<Integer> gamesId) {
        this.gamesId = gamesId;
    }

    public static GameListDto from(List<Integer> loadGames) {
        return new GameListDto(loadGames);
    }

    public List<Integer> getGamesId() {
        return gamesId;
    }
}
