package chess.web.controller.dto;

public class RoomRequestDto {

    private String title;
    private String password;

    private RoomRequestDto() {
    };

    public RoomRequestDto(String title, String password) {
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
