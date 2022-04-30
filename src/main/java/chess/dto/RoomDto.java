package chess.dto;

public class RoomDto {

    private String title;
    private String password;

    public RoomDto(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getTitle() {
        return title;
    }
}
