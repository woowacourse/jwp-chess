package chess.dto;

public class CreateRoomDto {

    private String title;
    private String password;

    public CreateRoomDto() {
    }

    public CreateRoomDto(String title, String password) {
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
