package chess.exception;

public class DuplicateRoomException extends ChessException {

    private static final String MESSAGE = "이미 존재하는 방 이름입니다.";

    public DuplicateRoomException() {
        super(MESSAGE);
    }
}
