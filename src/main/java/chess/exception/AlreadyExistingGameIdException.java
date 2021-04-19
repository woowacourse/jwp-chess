package chess.exception;

public class AlreadyExistingGameIdException extends ChessGameException {
    private static final String MESSAGE = "이미 존재하는 게임 아이디 입니다.";

    public AlreadyExistingGameIdException() {
        super(MESSAGE);
    }

}
