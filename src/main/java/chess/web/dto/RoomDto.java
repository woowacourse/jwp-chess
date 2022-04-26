package chess.web.dto;

public class RoomDto {

    private long id;
    private final String name;
    private final String password;

    public RoomDto(long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public RoomDto (String name, String password) {
        this.name = name;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
