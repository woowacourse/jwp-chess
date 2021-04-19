package chess.domain.exceptions;

public class InvalidMoveException extends IllegalArgumentException {

    public InvalidMoveException() {

    }

    public InvalidMoveException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
