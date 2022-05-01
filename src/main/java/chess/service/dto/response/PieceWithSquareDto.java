package chess.service.dto.response;

import chess.model.board.Square;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;

public class PieceWithSquareDto {
    private final String square;
    private final String type;
    private final String color;

    public PieceWithSquareDto(String square, String type, String color) {
        this.square = square;
        this.type = type;
        this.color = color;
    }

    public PieceWithSquareDto(Square square, Piece piece) {
        this(square.getName(), PieceType.getName(piece), piece.getColor().name());
    }

    public String getSquare() {
        return square;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }
}
