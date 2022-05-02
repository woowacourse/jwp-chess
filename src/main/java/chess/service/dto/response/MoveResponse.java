package chess.service.dto.response;

import static java.util.stream.Collectors.toMap;

import chess.model.board.MoveResult;
import chess.model.board.Square;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import java.util.Map;

public class MoveResponse {
    private Map<String, String> affectedPieces;
    private boolean isKingAttacked;

    public MoveResponse(MoveResult movedResult) {
        this.affectedPieces = convertToString(movedResult);
        this.isKingAttacked = movedResult.isKingAttacked();
    }

    private Map<String, String> convertToString(MoveResult movedResult) {
        Map<Square, Piece> affectedPiece = movedResult.getAffectedPiece();
        return affectedPiece.keySet().stream()
                .collect(toMap(Square::getName, square -> PieceType.getName(affectedPiece.get(square))));
    }

    public Map<String, String> getAffectedPieces() {
        return affectedPieces;
    }

    public boolean isKingAttacked() {
        return isKingAttacked;
    }
}
