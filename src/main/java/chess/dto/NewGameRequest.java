package chess.dto;

public class NewGameRequest {

    private String roomName;
    private String roomPassword;
    private String whiteName;
    private String blackName;

    public String getRoomName() {
        return roomName;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public String getBlackName() {
        return blackName;
    }
}
