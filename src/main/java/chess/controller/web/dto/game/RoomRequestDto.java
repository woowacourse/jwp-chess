package chess.controller.web.dto.game;

public class RoomRequestDto {

    private String roomName;
    private String whiteUsername;
    private String whitePassword;

    public RoomRequestDto(String whiteUsername, String whitePassword, String roomName) {
        this.whiteUsername = whiteUsername;
        this.whitePassword = whitePassword;
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getWhitePassword() {
        return whitePassword;
    }
}
