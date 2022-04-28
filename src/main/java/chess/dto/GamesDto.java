package chess.dto;

import java.util.List;

public class GamesDto {

    private final List<RoomDto> games;

    public GamesDto(List<RoomDto> games) {
        this.games = games;
    }

    public List<RoomDto> getGames() {
        return games;
    }
}
