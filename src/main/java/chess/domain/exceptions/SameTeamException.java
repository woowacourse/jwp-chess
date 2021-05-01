package chess.domain.exceptions;

import org.springframework.http.HttpStatus;

public class SameTeamException extends ChessException {

    public SameTeamException() {
        super(HttpStatus.BAD_REQUEST, "같은 팀의 말입니다.");
    }
}
