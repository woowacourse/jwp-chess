package chess.web;

public class ChessForm {

    private String roomName;
    private String password;

    private ChessForm() {}

    public String getRoomName() {
        return roomName;
    }

    public String getPassword() {
        return password;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
