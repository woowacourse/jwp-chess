package chess.repository.entity;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.Objects;

public class PieceEntity {

    private final String position;
    private final String color;
    private final String role;

    public PieceEntity(String position, String color, String symbol) {
        this.position = position.toLowerCase();
        this.color = color.toLowerCase();
        this.role = symbol.toLowerCase();
    }

    public static PieceEntity from(Position position, Piece piece) {
        return new PieceEntity(position.name(), piece.getColor().name(), piece.symbol());
    }

    public String getPosition() {
        return position;
    }

    public String getColor() {
        return color;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PieceEntity pieceEntity = (PieceEntity) o;
        return Objects.equals(color, pieceEntity.color) && Objects.equals(role, pieceEntity.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, role);
    }
}
