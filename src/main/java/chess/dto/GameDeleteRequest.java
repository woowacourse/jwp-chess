package chess.dto;

public class GameDeleteRequest {

    private int id;
    private String password;

    public GameDeleteRequest() {
    }

    public GameDeleteRequest(int id, String password) {
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
