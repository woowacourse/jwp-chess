package chess.dto;

public class DeleteGameRequest {

    private String title;
    private String password;

    private DeleteGameRequest() {
    }

    public DeleteGameRequest(String title, String password) {
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
