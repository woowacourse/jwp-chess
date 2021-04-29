package chess.exception;

public class NoWinnerException extends IllegalArgumentException {

    public NoWinnerException() {
        super("우승자가 없습니다.");
    }

}
