package chess.dto;

public class RoomDto {

    private final String name;
    private final String password;

    public RoomDto(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
