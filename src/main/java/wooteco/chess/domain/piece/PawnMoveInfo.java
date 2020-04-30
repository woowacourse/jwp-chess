package wooteco.chess.domain.piece;

import java.util.Arrays;

import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Row;

public enum PawnMoveInfo {
	LOWER(Turn.WHITE, Row.TWO, Row.FOUR, -1),
	UPPER(Turn.BLACK, Row.SEVEN, Row.FIVE, 1);

	private final Turn turn;
	private final Row initial;
	private final Row jumped;
	private final int rowGap;

	PawnMoveInfo(Turn turn, Row initial, Row jumped, int rowGap) {
		this.turn = turn;
		this.initial = initial;
		this.jumped = jumped;
		this.rowGap = rowGap;
	}

	public static PawnMoveInfo of(Turn turn) {
		return Arrays.stream(values())
			.filter(value -> value.turn.equals(turn))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("정의 되지 않은 폰의 팀입니다."));
	}

	public boolean isInitialRow(Position position) {
		return position.isRowEquals(initial);
	}

	public Position getJumpedPositionOf(Position position) {
		return Position.of(position.getColumn(), jumped);
	}

	public boolean isValidRowGap(int rowGap) {
		return this.rowGap == rowGap;
	}
}