package chess.repository.dao.entity;

import chess.model.board.Square;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import java.util.Objects;

public class PieceEntity {
    private final Integer id;
    private final Integer gameId;
    private final String square;
    private final String type;
    private final String color;

    public PieceEntity(final Integer gameId, final Square square, final Piece piece) {
        this.id = null;
        this.gameId = gameId;
        this.square = square.getName();
        this.type = PieceType.getName(piece);
        this.color = piece.getColor().name();
    }

    public PieceEntity(final Integer id, final Integer gameId, final String square, final String type,
                       final String color) {
        this.id = id;
        this.gameId = gameId;
        this.square = square;
        this.type = type;
        this.color = color;
    }

    public Integer getId() {
        return id;
    }

    public Integer getGameId() {
        return gameId;
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
