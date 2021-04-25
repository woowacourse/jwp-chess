package chess.exception;

public class NotExistRoomException extends RuntimeException {

    public static final String DOES_NOT_EXIST_ROOM = "올바르지 않은 방 제목입니다. (DB 접근 오류)";

    public NotExistRoomException() {
        super(DOES_NOT_EXIST_ROOM);
    }
}
