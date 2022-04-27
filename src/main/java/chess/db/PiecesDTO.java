package chess.db;

public class PiecesDTO {
    private final String location;
    private final String color;
    private final String name;

    public PiecesDTO(String location, String color, String name) {
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
