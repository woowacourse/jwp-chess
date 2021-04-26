package chess.exception;

public class NotExistUserException extends UserException {

    private static final String MESSAGE = "존재하지 않는 유저 이름입니다.";

    public NotExistUserException() {
        super(MESSAGE);
    }
}
