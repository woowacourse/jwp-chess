package wooteco.chess.dto;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

public class TileDto {
    private final Position position;
    private final Piece piece;

    public TileDto(Position position, Piece piece) {
        this.position = position;
        this.piece = piece;
    }

    public Position getPosition() {
        return position;
    }

    public Piece getPiece() {
        return piece;
    }
}
