package chess.web.service.dto;

public class CreateRoomDto {

    private String title;
    private String password;

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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
