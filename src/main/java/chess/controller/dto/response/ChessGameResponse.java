package chess.controller.dto.response;

import chess.domain.ChessGame;

public class ChessGameResponse {

    private long id;
    private String title;

    private ChessGameResponse() {
    }

    private ChessGameResponse(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static ChessGameResponse of(ChessGame chessGame) {
        return new ChessGameResponse(chessGame.getId(), chessGame.getTitle());
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
