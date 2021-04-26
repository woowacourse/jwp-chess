package chess.exception;

public class PasswordIncorrectException extends ChessException{

    private static final String MESSAGE = "비밀번호가 틀렸습니다.";

    public PasswordIncorrectException() {
        super(MESSAGE);
    }
}
