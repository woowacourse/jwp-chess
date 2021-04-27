package chess.domain.exceptions;

import org.springframework.http.HttpStatus;

public class WrongTurnException extends ChessException {

    public WrongTurnException() {
        super(HttpStatus.BAD_REQUEST, "해당 턴이 아닙니다.");
    }
}
