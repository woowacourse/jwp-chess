package chess.dto;

public class MakeRoomDto {

    private final String name;
    private final String password;

    public MakeRoomDto(String name, String password) {
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