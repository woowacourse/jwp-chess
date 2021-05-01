package chess.domain.exceptions;

import org.springframework.http.HttpStatus;

public class UnableMoveTypeException extends ChessException {

    public UnableMoveTypeException() {
        super(HttpStatus.BAD_REQUEST, "해당 말이 이동할 수 있는 위치가 아닙니다.");
    }
}
