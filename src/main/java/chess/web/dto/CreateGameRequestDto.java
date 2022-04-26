package chess.web.dto;

public class CreateGameRequestDto {

    private final String title;
    private final String password;

    public CreateGameRequestDto(String title, String password) {
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
