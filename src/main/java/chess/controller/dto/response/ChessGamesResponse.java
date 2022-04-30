package chess.controller.dto.response;

import java.util.List;

public class ChessGamesResponse {

    private final List<GameRoomResponse> games;

    public ChessGamesResponse(List<GameRoomResponse> games) {
        this.games = games;
    }

    public List<GameRoomResponse> getGames() {
        return games;
    }
}
