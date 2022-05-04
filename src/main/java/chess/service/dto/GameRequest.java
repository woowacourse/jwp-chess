package chess.service.dto;

public class GameRequest {

    private String password;

    public GameRequest() {}

    public GameRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
