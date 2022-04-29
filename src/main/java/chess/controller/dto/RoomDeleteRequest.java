package chess.controller.dto;

public class RoomDeleteRequest {

    private final String password;

    RoomDeleteRequest() {
        this.password = "";
    }

    public RoomDeleteRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
