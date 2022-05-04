package chess.web.dto;

public class CreateRoomDto {
    private String roomName;
    private String password;

    public CreateRoomDto() {
    }

    public CreateRoomDto(String roomName, String password) {
        this.roomName = roomName;
        this.password = password;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPassword() {
        return password;
    }
}
