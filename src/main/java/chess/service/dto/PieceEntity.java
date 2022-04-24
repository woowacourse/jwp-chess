package chess.service.dto;

import chess.model.board.Square;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import java.util.Objects;

public class PieceEntity {
    private int id;
    private String square;
    private String type;
    private String color;
    private int gameId;


    public PieceEntity() {
    }

    public PieceEntity(Square square, Piece piece) {
        this.square = square.getName();
        this.type = PieceType.getName(piece);
        this.color = piece.getColor().name();
    }

    public PieceEntity(String square, String type, String color) {
        this.square = square;
        this.type = type;
        this.color = color;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PieceEntity that = (PieceEntity) o;
        return Objects.equals(getSquare(), that.getSquare()) && Objects.equals(
            getType(), that.getType()) && Objects.equals(getColor(), that.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSquare(), getType(), getColor());
    }
}
