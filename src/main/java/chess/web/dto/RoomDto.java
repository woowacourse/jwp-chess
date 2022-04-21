package chess.web.dto;

public class RoomDto {

    private long id;
    private String name;

    public RoomDto(long id, String name) {
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
