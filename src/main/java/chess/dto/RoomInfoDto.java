package chess.dto;

public class RoomInfoDto {

    private final long id;
    private final String name;

    public RoomInfoDto(long id) {
        this(id, null);
    }

    public RoomInfoDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
