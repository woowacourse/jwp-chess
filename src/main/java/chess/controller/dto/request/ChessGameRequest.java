package chess.controller.dto.request;

public class ChessGameRequest {

    private String title;
    private String password;

    private ChessGameRequest() {
    }

    public ChessGameRequest(String title, String password) {
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
