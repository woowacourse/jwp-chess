package chess.dto;

public class RoomDto {
    private final int id;
    private final String name;

    public RoomDto(final int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
