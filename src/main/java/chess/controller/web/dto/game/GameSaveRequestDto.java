package chess.controller.web.dto.game;

public class GameSaveRequestDto {

    private String whiteUsername;
    private String whitePassword;
    private String roomName;

    public GameSaveRequestDto(String whiteUsername, String whitePassword, String roomName) {
        this.whiteUsername = whiteUsername;
        this.whitePassword = whitePassword;
        this.roomName = roomName;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getWhitePassword() {
        return whitePassword;
    }

    public String getRoomName() {
        return roomName;
    }
}
