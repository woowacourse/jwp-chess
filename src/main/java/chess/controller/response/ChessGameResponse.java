package chess.controller.response;

import chess.serviece.dto.PieceDto;
import chess.serviece.dto.GameDto;

import java.util.List;
import java.util.stream.Collectors;

public class ChessGameResponse {

    private final String turn;
    private final String status;
    private final List<PieceResponse> pieces;

    private ChessGameResponse(List<PieceResponse> pieces, String turn, String status) {
        this.pieces = pieces;
        this.turn = turn;
        this.status = status;
    }

    public static ChessGameResponse from(GameDto game, List<PieceDto> pieces) {
        List<PieceResponse> pieceResponses = pieces.stream()
                .map(PieceResponse::from)
                .collect(Collectors.toList());
        return new ChessGameResponse(pieceResponses, game.getTurn(), game.getStatus());
    }

    public List<PieceResponse> getPieces() {
        return pieces;
    }

    public String getTurn() {
        return turn;
    }

    public String getStatus() {
        return status;
    }
}
