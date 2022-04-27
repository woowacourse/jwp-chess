package chess.dto;

public class GameDto {

    private int id;
    private final String roomTitle;
    private final String password;

    public GameDto(String roomTitle, String password) {
        this.roomTitle = roomTitle;
        this.password = password;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public String getPassword() {
        return password;
    }
}
