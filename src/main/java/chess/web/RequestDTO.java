package chess.web;

public class RequestDTO {

    private String roomId;
    private String command;

    public String getCommand() {
        return command;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
