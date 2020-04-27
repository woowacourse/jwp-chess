package wooteco.chess.exception;

public class DuplicateRoomNameException extends IllegalArgumentException {
    public DuplicateRoomNameException(String message) {
        super(message);
    }
}
