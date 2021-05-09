package chess.webdto.view;

public class RoomNameDto {
    private String roomName;
    private String password;

    public RoomNameDto() {
    }

    public RoomNameDto(String roomName, String password) {
        this.roomName = roomName;
        this.password = password;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
