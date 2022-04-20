package chess.dto;

import chess.domain.piece.Piece;

public class PieceDto {

    private final String name;
    private final String rawSquare;

    private PieceDto(final String name, final String rawSquare) {
        this.name = name;
        this.rawSquare = rawSquare;
    }

    public static PieceDto toDto(final Piece piece) {
        String rawSquare = piece.getSquare().toRawSquare();

        if (piece.isBlack()) {
            return new PieceDto("black_" + piece.getPieceType().getSymbol(), rawSquare);
        }
        if (piece.isWhite()) {
            return new PieceDto("white_" + piece.getPieceType().getSymbol(), rawSquare);
        }
        return new PieceDto("blank", rawSquare);
    }

    public String getName() {
        return name;
    }

    public String getRawSquare() {
        return rawSquare;
    }
}
