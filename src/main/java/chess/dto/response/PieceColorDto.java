package chess.dto.response;

import chess.domain.piece.PieceColor;

public class PieceColorDto {
    private final PieceColor pieceColor;

    private PieceColorDto(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    public static PieceColorDto from(PieceColor pieceColor) {
        return new PieceColorDto(pieceColor);
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }
}

