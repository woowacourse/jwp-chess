package chess.dto;

public class GameCreateDto {

    private String roomName;
    private String password;

    public GameCreateDto() {
    }

    public GameCreateDto(String roomName, String password) {
        this.roomName = roomName;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
