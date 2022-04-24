package chess.dto;

import chess.domain.piece.Piece;
import chess.domain.position.Position;

public final class PieceAndPositionDto {

    private final String pieceName;
    private final String pieceColor;
    private final String position;

    public PieceAndPositionDto(final Position position, final Piece piece) {
        this.pieceName = piece.getNotation().name();
        this.pieceColor = piece.getColor().name();
        this.position = position.getFile().name() + position.getRankNumber();
    }

    public PieceAndPositionDto(final String pieceName, final String pieceColor, final String position) {
        this.pieceName = pieceName;
        this.pieceColor = pieceColor;
        this.position = position;
    }

    public String getPieceName() {
        return pieceName;
    }

    public String getPieceColor() {
        return pieceColor;
    }

    public String getPosition() {
        return position;
    }
}
