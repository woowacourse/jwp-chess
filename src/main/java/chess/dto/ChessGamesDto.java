package chess.dto;

import java.util.List;

public class ChessGamesDto {
    private final List<ChessGameDto> gameList;

    public ChessGamesDto(List<ChessGameDto> gameList) {
        this.gameList = gameList;
    }

    public List<ChessGameDto> getGameList() {
        return gameList;
    }
}
