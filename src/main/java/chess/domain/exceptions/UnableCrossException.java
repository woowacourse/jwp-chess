package chess.domain.exceptions;

import org.springframework.http.HttpStatus;

public class UnableCrossException extends ChessException {

    public UnableCrossException() {
        super(HttpStatus.BAD_REQUEST, "해당 말은 뛰어넘기가 불가합니다.");
    }
}
