package chess.dto.response;

import chess.domain.piece.Piece;
import chess.domain.position.Position;

public class PieceResponseDto {

    private final String position;
    private final String name;

    public PieceResponseDto(Position position, Piece piece) {
        this.position = position.chessCoordinate();
        this.name = piece.getName();
    }

    public String getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

}
