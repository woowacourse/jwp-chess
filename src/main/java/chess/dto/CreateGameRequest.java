package chess.dto;

public class CreateGameRequest {

    private final String title;
    private final String password;

    public CreateGameRequest(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "CreateGameRequest{" +
                "title='" + title + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
