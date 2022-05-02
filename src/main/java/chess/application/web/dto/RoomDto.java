package chess.application.web.dto;

public class RoomDto {

    private int id;
    private String name;

    public RoomDto(final int roomId, final String name) {
        this.id = roomId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
