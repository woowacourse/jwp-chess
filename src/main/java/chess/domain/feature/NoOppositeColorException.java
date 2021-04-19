package chess.domain.feature;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Blank Does Not Have Opposite Color")
public class NoOppositeColorException extends RuntimeException {
    private static final String NO_OPPOSITE_COLOR_ERROR = "공백은 색깔이 없습니다.";

    public NoOppositeColorException() {
        super(NO_OPPOSITE_COLOR_ERROR);
    }
}
