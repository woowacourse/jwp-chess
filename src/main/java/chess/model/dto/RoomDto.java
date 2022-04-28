package chess.model.dto;

public class RoomDto {

    private final String title;
    private final String password;

    public RoomDto(String title, String password) {
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
