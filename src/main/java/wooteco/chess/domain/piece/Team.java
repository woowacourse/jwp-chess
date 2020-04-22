package wooteco.chess.domain.piece;

public enum Team {
	BLACK,
	WHITE,
	NONE;

	public Team next() {
		if (BLACK == this) {
			return WHITE;
		}
		if (WHITE == this) {
			return BLACK;
		}
		throw new IllegalArgumentException("잘못된 팀 입력입니다.");
	}

	public boolean isEnemy(Team team) {
		if (BLACK == this) {
			return WHITE == team;
		}
		if (WHITE == this) {
			return BLACK == team;
		}
		throw new IllegalArgumentException("잘못된 팀 입력입니다.");
	}

	public boolean isNotEnemy(Team team) {
		return !isEnemy(team);
	}
}
