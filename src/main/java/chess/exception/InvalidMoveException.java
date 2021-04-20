package chess.exception;

public class InvalidMoveException extends RuntimeException {

    public InvalidMoveException() {
        super("[ERROR] 해당 위치로 이동할 수 없습니다.");
    }
}
