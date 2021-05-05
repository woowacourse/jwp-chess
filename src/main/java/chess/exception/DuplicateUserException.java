package chess.exception;

public class DuplicateUserException extends UserException {

    private static final String MESSAGE = "이미 존재하는 유저 이름입니다.";

    public DuplicateUserException() {
        super(MESSAGE);
    }
}
