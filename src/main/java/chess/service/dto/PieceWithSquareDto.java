package chess.service.dto;

import java.util.Objects;

public class PieceWithSquareDto {
    private final String square;
    private final String type;
    private final String color;

    public PieceWithSquareDto(String square, String type, String color) {
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
        PieceWithSquareDto that = (PieceWithSquareDto) o;
        return Objects.equals(getSquare(), that.getSquare()) && Objects.equals(
            getType(), that.getType()) && Objects.equals(getColor(), that.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSquare(), getType(), getColor());
    }
}
