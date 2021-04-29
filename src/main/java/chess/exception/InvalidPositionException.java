package chess.exception;

public class InvalidPositionException extends InvalidChessObjectException {

    public InvalidPositionException() {
        super("존재하지 않는 열(또는 행)입니다");
    }
}
