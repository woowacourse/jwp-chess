package chess.dto;

import java.util.List;

public class GameListDto {
    private final List<GameEntryDto> games;

    private GameListDto(List<GameEntryDto> games) {
        this.games = games;
    }

    public static GameListDto from(List<GameEntryDto> loadGames) {
        return new GameListDto(loadGames);
    }

    public List<GameEntryDto> getGames() {
        return games;
    }
}
