package wooteco.chess.domain.position;

public enum Row {
	ONE("1"),
	TWO("2"),
	THREE("3"),
	FOUR("4"),
	FIVE("5"),
	SIX("6"),
	SEVEN("7"),
	EIGHT("8");

	private static final int STEP = 1;

	private final String name;

	Row(String name) {
		this.name = name;
	}

	public Row plus() {
		try {
			return values()[ordinal() + STEP];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("9 이상의 row 값은 가질수 없습니다.");
		}
	}

	public Row minus() {
		try {
			return values()[ordinal() - STEP];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("0 이하의 row 값은 가질수 없습니다.");
		}
	}

	public String getName() {
		return name;
	}
}
