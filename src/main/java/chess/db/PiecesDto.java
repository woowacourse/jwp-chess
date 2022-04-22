package chess.db;

public class PiecesDto {
    private final String location;
    private final String color;
    private final String name;

    public PiecesDto(String location, String color, String name) {
        this.location = location;
        this.color = color;
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
