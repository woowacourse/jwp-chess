package chess.domain.piece;

import chess.domain.feature.Color;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Color Assignment To Blank")
public class BlankColorException extends RuntimeException {
    public static final String BLANK_COLOR_ERROR = "공백의 색깔은 " + Color.NO_COLOR.name() + "여야 합니다.";

    public BlankColorException() {
        super(BLANK_COLOR_ERROR);
    }
}
