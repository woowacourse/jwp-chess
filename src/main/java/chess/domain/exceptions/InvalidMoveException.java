package chess.domain.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidMoveException extends ChessException {

    public InvalidMoveException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 움직임 입니다.");
    }

    public InvalidMoveException(String exceptionMessage) {
        super(HttpStatus.BAD_REQUEST, exceptionMessage);
    }
}
