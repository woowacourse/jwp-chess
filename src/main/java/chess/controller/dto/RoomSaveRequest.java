package chess.controller.dto;

public class RoomSaveRequest {

    private final String name;
    private final String password;

    RoomSaveRequest() {
        this.name = "";
        this.password = "";
    }

    public RoomSaveRequest(String name, String password) {
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
