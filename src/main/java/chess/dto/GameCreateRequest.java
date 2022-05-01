package chess.dto;

public class GameCreateRequest {

    private String roomName;
    private String roomPassword;
    private String whiteName;
    private String blackName;

    public GameCreateRequest() {
    }

    public GameCreateRequest(String roomName, String roomPassword, String whiteName, String blackName) {
        this.roomName = roomName;
        this.roomPassword = roomPassword;
        this.whiteName = whiteName;
        this.blackName = blackName;
    }

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

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }
}
