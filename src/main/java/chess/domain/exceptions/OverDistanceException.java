package chess.domain.exceptions;

import org.springframework.http.HttpStatus;

public class OverDistanceException extends ChessException {

    public OverDistanceException() {
        super(HttpStatus.BAD_REQUEST, "해당 말의 이동 가능한 거리를 초과했습니다.");
    }
}
