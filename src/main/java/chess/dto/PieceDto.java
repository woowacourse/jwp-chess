package chess.dto;

import java.util.List;

public final class PieceDto {

    private final String color;
    private final String name;

    public PieceDto(final List<String> value) {
        this.name = value.get(0);
        this.color = value.get(1);
    }

    public PieceDto(String name, String color) {
        this.name = name;
        this.color = color;

    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
