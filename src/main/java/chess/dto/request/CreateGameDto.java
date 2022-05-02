package chess.dto.request;

public class CreateGameDto {

    private final String chessGameName;
    private final String password;
    private final String passwordCheck;

    public CreateGameDto(String chessGameName, String password, String passwordCheck) {
        this.chessGameName = chessGameName;
        this.password = password;
        this.passwordCheck = passwordCheck;
    }

    public String getChessGameName() {
        return chessGameName;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordCheck() {
        return passwordCheck;
    }
}
