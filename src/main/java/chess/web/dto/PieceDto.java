package chess.web.dto;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.Objects;

public class PieceDto {

    private final String position;
    private final String color;
    private final String role;

    public PieceDto(String position, String color, String symbol) {
        this.position = position.toLowerCase();
        this.color = color.toLowerCase();
        this.role = symbol.toLowerCase();
    }

    public static PieceDto from(Position position, Piece piece) {
        return new PieceDto(position.name(), piece.getColor().name(), piece.symbol());
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
        PieceDto pieceDto = (PieceDto) o;
        return Objects.equals(color, pieceDto.color) && Objects.equals(role, pieceDto.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, role);
    }
}
