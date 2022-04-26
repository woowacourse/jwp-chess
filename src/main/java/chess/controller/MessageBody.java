package chess.controller;

public class MessageBody {

    private final String roomName;
    private final String whiteName;
    private final String blackName;

    public MessageBody(String roomName, String whiteName, String blackName) {
        this.roomName = roomName;
        this.whiteName = whiteName;
        this.blackName = blackName;
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
}
