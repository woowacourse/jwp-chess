package chess.domain.exceptions;

public class SameTeamException extends IllegalArgumentException {

    public SameTeamException() {
        super("같은 팀의 말입니다.");
    }
}
