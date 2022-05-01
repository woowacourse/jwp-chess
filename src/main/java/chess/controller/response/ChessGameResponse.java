package chess.controller.response;

import chess.dto.PieceDto;

import java.util.List;

public class ChessGameResponse {

    private final Long gameId;
    private final List<PieceDto> pieces;
    private final String turn;
    private final String status;

    public ChessGameResponse(Long gameId, List<PieceDto> pieces, String turn, String status) {
        this.gameId = gameId;
        this.pieces = pieces;
        this.turn = turn;
        this.status = status;
    }

    public List<PieceDto> getPieces() {
        return pieces;
    }

    public String getTurn() {
        return turn;
    }

    public String getStatus() {
        return status;
    }
}
