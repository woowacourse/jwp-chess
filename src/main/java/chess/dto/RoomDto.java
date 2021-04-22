package chess.dto;

public class RoomDto {
    private String roomId;
    private String roomName;
    private String roomPassword;

    public RoomDto(String roomName, String roomPassword) {
        this.roomName = roomName;
        this.roomPassword = roomPassword;
    }

    public RoomDto(String roomId, String roomName, String roomPassword) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomPassword = roomPassword;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }
}
