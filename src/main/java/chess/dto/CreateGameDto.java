package chess.dto;

public class CreateGameDto {

    private final String chessGameName;
    private final String password;
    private final String passwordCheck;

    public CreateGameDto(String chessGameName, String password, String passwordCheck) {
        validatePassword(password, passwordCheck);
        this.chessGameName = chessGameName;
        this.password = password;
        this.passwordCheck = passwordCheck;
    }

    private void validatePassword(final String password, final String passwordCheck) {
        boolean isSame = password.equals(passwordCheck);
        if (!isSame) {
            throw new IllegalArgumentException("패스워드를 다시 확인해주세요.");
        }
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
