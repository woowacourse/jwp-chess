package chess.controller.dto;

public class RoomDeleteRequest {

    private final String password;

    public RoomDeleteRequest() {
        this.password = "";
    }

    public String getPassword() {
        return password;
    }
}
