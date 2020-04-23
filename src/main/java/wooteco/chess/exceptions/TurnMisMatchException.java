package wooteco.chess.exceptions;

public class TurnMisMatchException extends RuntimeException {
	public TurnMisMatchException() {
		super("자신의 턴이 아닙니다.");
	}
}
