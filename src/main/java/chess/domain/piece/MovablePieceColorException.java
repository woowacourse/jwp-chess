package chess.domain.piece;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Color Assignment To Movable Pieces")
public class MovablePieceColorException extends RuntimeException {
    public static final String INVALID_PIECE_COLOR_ERROR = "체스말의 색깔은 흑이나 백이어야 합니다.";

    public MovablePieceColorException() {
        super(INVALID_PIECE_COLOR_ERROR);
    }
}
