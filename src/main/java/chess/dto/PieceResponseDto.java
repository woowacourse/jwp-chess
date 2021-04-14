package chess.dto;

import chess.domain.piece.Piece;
import chess.domain.position.Position;

public class PieceResponseDto {
    private final String position;
    private final String name;

    public String getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public PieceResponseDto(final Position position, final Piece piece) {
        this.position = position.chessCoordinate();
        this.name = piece.getName();
    }
}
