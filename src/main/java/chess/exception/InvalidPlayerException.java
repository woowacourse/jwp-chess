package chess.exception;

public class InvalidPlayerException extends InvalidChessObjectException {

    public InvalidPlayerException() {
        super("해당 색을 가진 플레이어가 존재하지 않습니다.");
    }
}
