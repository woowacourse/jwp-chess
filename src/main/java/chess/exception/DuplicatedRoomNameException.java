package chess.exception;

public class DuplicatedRoomNameException extends ChessGameException {
    public static final String DUPLICATED_ROOM_NAME_ERROR = "중복된 방 이름입니다.";

    public DuplicatedRoomNameException() {
        super(DUPLICATED_ROOM_NAME_ERROR);
    }
}
