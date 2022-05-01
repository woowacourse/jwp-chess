package chess.model.dto;

public class RoomDto {

    private String title;
    private String password;

    public RoomDto() {
    }

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
