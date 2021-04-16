package chess.service.dto;

import chess.domain.piece.Piece;

public class PieceStatusDto {

    private final String position;
    private final String pieceName;

    public PieceStatusDto(final Piece piece) {
        this.position = piece.positionInfo();
        this.pieceName = piece.name();
    }

    public String getPieceName() {
        return pieceName;
    }

    public String getPosition() {
        return position;
    }
}
