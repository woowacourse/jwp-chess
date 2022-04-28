package chess.dto;

public class RoomInfoDto {
    private final String title;
    private final String password;

    public RoomInfoDto(String title, String password) {
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
