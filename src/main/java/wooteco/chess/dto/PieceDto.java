package wooteco.chess.dto;

import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceType;

public class PieceDto {
    private final PieceType pieceType;
    private final Color color;

    public PieceDto(Piece piece) {
        this.pieceType = piece.getPieceType();
        this.color = piece.getColor();
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public Color getColor() {
        return color;
    }
}