package chess.dto;

public class PieceDto {
    private final String position;
    private final String type;
    private final String color;

    public PieceDto(String position, String type, String color) {
        this.position = position;
        this.type = type;
        this.color = color;
    }

    public String getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }
}
