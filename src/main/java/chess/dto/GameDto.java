package chess.dto;

public class GameDto {

    private int id;
    private String roomTitle;
    private String password;

    public GameDto() {
    }

    public GameDto(String roomTitle, String password) {
        this.roomTitle = roomTitle;
        this.password = password;
    }

    public GameDto(int id, String roomTitle) {
        this.id = id;
        this.roomTitle = roomTitle;
    }

    public int getId() {
        return id;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public String getPassword() {
        return password;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
