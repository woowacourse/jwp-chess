package chess.dto;

public final class PieceDto {

    private final String color;
    private final String name;

    public PieceDto(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
