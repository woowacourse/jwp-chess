package chess.controller;

public class MessageBody {

    private final String roomName;
    private final String whiteName;
    private final String blackName;
    private final String password;

    public MessageBody(String roomName, String whiteName, String blackName, String password) {
        this.roomName = roomName;
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.password = password;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public String getBlackName() {
        return blackName;
    }

    public String getPassword() {
        return password;
    }
}
