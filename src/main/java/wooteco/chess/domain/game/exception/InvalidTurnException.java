package wooteco.chess.domain.game.exception;

import wooteco.chess.domain.game.Turn;

public class InvalidTurnException extends RuntimeException {
	public InvalidTurnException(Turn turn) {
		super(turn.getColor().name() + "의 턴 입니다.");
	}
}
