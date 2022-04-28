package chess.dto.response;

import java.util.List;

public class GamesResponse {

    private final List<RoomResponse> games;

    public GamesResponse(List<RoomResponse> games) {
        this.games = games;
    }

    public List<RoomResponse> getGames() {
        return games;
    }
}
