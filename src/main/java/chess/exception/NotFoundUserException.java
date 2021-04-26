package chess.exception;

public class NotFoundUserException extends UserException{

    private static final String MESSAGE = "상대 유저가 아직 들어오지 않았습니다.";

    public NotFoundUserException() {
        super(MESSAGE);
    }
}
