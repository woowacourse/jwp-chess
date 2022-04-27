package chess.controller.dto;

public class RoomSaveRequest {

    private final String name;
    private final String password;

    RoomSaveRequest() {
        this.name = "";
        this.password = "";
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
