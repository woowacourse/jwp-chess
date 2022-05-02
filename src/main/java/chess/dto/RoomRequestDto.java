package chess.dto;

public class RoomRequestDto {
    private String name;
    private String password;

    private RoomRequestDto() {
    }

    public RoomRequestDto(String name, String password) {
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
