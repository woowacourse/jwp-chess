package chess.dto;

public class DeleteGameRequest {

    private int id;
    private String title;
    private String password;

    private DeleteGameRequest() {
    }

    public DeleteGameRequest(int id, String title, String password) {
        this.id = id;
        this.title = title;
        this.password = password;
    }

    public int getId() {
        return id;
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
