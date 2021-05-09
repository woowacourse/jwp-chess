package chess.service.dto.game;

public class RoomSaveDto {

    private String roomName;
    private String whiteUsername;
    private String whitePassword;

    public RoomSaveDto() {
    }

    public RoomSaveDto(String roomName, String whiteUsername, String whitePassword) {
        this.roomName = roomName;
        this.whiteUsername = whiteUsername;
        this.whitePassword = whitePassword;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getWhitePassword() {
        return whitePassword;
    }

    public void setWhitePassword(String whitePassword) {
        this.whitePassword = whitePassword;
    }
}
