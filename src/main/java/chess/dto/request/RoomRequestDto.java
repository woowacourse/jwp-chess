package chess.dto.request;

public class RoomRequestDto {

    private String name;
    private String password;

    public RoomRequestDto() {
    }

    public RoomRequestDto(final String name, final String password) {
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
