package chess.dto;

public class GameDto {

    private int id;
    private String roomName;
    private String password;
    private String state;

    public GameDto() {
    }

    public GameDto(String roomName, String password, String state) {
        this.roomName = roomName;
        this.password = password;
        this.state = state;
    }

    public GameDto(int id, String roomName, String state) {
        this.id = id;
        this.roomName = roomName;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPassword() {
        return password;
    }

    public String getState() {
        return state;
    }
}
