package chess.dto;

public class GameRoomDto {

    private String title;
    private String password;

    public GameRoomDto(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

}
