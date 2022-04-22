package chess.dto;

public class PieceDto {

    private String position;
    private String color;
    private String name;

    public PieceDto() {

    }

    public PieceDto(String position, String color, String name) {
        this.position = position;
        this.color = color;
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

}
