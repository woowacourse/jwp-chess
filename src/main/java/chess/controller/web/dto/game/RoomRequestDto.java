package chess.controller.web.dto.game;

public class RoomRequestDto {

    private String roomName;
    private String whiteUsername;
    private String whitePassword;

    public RoomRequestDto(final String whiteUsername, final String whitePassword, final String roomName) {
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
