package dto;

import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PiecesDto {

    private final Map<String, Piece> pieces;

    public PiecesDto(Map<String, Piece> pieces) {
        this.pieces = Collections.unmodifiableMap(pieces);
    }

    public static PiecesDto of(Pieces pieces) {
        Map<String, Piece> pieceMap = new HashMap<>();
        for (Piece piece : pieces.getPieces()) {
            pieceMap.put(piece.getPosition().getPosition(), piece);
        }
        return new PiecesDto(pieceMap);
    }

    public Map<String, Piece> getPieces() {
        return pieces;
    }
}
