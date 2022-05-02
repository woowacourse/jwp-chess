package chess.serviece.dto;

import chess.domain.piece.Piece;
import chess.domain.position.Position;

import java.util.Objects;

public class PieceDto {

    private String position;
    private String color;
    private String type;
    private Long gameId;

    public PieceDto(String position, String color, String type, Long gameId) {
        this.position = position;
        this.color = color;
        this.type = type;
        this.gameId = gameId;
    }

    public PieceDto(String position, String color, String type) {
        this.position = position;
        this.color = color;
        this.type = type;
    }

    public static PieceDto from(Position position, Piece piece, Long gameId) {
        return new PieceDto(position.getName(), piece.getColor().getName(), piece.getType().getName(), gameId);
    }

    public static PieceDto of(String position, String color, String type) {
        return new PieceDto(position, color, type);
    }

    public String getPosition() {
        return position;
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public Long getGameId() {
        return gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PieceDto pieceDto = (PieceDto) o;
        return Objects.equals(position, pieceDto.position) && Objects.equals(color, pieceDto.color) && Objects.equals(type, pieceDto.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, color, type);
    }
}
