package wooteco.chess.domain.piece;

public enum Turn {
	BLACK,
	WHITE,
	NONE;

	public Turn next() {
		if (BLACK == this) {
			return WHITE;
		}
		if (WHITE == this) {
			return BLACK;
		}
		throw new IllegalArgumentException("잘못된 팀 입력입니다.");
	}

	public boolean isEnemy(Turn turn) {
		if (BLACK == this) {
			return WHITE == turn;
		}
		if (WHITE == this) {
			return BLACK == turn;
		}
		throw new IllegalArgumentException("잘못된 팀 입력입니다.");
	}

	public boolean isNotEnemy(Turn turn) {
		return !isEnemy(turn);
	}
}
