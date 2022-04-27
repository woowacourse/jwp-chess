package chess.dto;

public class ChessGameRequest {

    private String name;

    private String password;

    public ChessGameRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
