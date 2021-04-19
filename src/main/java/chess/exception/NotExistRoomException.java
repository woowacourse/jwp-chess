package chess.exception;

public class NotExistRoomException extends ChessException {

    private static final String MESSAGE = "없는 게임 이름입니다";

    public NotExistRoomException() {
        super(MESSAGE);
    }
}
