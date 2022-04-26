package chess.controller.dto.request;

public class CreateGameRequest {

    private String gameName;
    private String password;

    public CreateGameRequest() {
    }

    public CreateGameRequest(String gameName, String password) {
        this.gameName = gameName;
        this.password = password;
    }

    public String getGameName() {
        return gameName;
    }

    public String getPassword() {
        return password;
    }
}
