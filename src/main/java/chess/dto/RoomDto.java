package chess.dto;

public class RoomDto {

    private final String id;
    private final String name;

    public RoomDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
