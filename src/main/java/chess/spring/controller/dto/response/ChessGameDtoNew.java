package chess.spring.controller.dto.response;

import chess.spring.domain.ChessGameNew;

public class ChessGameDtoNew {
    private final Long gameId;
    private final String title;

    public ChessGameDtoNew(ChessGameNew chessGame) {
        gameId = chessGame.getId();
        title = chessGame.getTitle();
    }

    public Long getGameId() {
        return gameId;
    }

    public String getTitle() {
        return title;
    }
}
