package chess.controller.dto.response;

import java.util.List;

public class ChessGamesResponse {

    private final List<GameIdentifiers> games;

    public ChessGamesResponse(List<GameIdentifiers> games) {
        this.games = games;
    }

    public List<GameIdentifiers> getGames() {
        return games;
    }
}
