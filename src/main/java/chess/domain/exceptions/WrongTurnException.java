package chess.domain.exceptions;

public class WrongTurnException extends IllegalArgumentException {

    public WrongTurnException() {
        super("해당 턴이 아닙니다.");
    }
}
