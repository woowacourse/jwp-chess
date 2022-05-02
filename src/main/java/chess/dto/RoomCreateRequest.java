package chess.dto;

public class RoomCreateRequest {

    private String name;

    private String password;

    public RoomCreateRequest(String name, String password) {
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
