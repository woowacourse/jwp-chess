package chess.service.dto.request;

public class DeleteGameRequest {
    private int id;
    private String password;

    public DeleteGameRequest() {
    }

    public DeleteGameRequest(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
