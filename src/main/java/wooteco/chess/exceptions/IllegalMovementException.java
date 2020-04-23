package wooteco.chess.exceptions;

public class IllegalMovementException extends RuntimeException {
	public IllegalMovementException() {
		super("이동할 수 없는 위치입니다.");
	}
}
