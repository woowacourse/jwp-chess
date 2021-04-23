package chess.exception;

public class NotExistRoomException extends RuntimeException {

    public NotExistRoomException() {
        super("방이 존재하지 않습니다.");
    }
}
