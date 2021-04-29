package chess.exception;

public class InvalidSourceException extends InvalidChessObjectException {

    public InvalidSourceException() {
        super("움직일 수 없는 말을 선택했습니다.");
    }
}
