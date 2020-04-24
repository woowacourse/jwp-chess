package wooteco.chess.domain.position;

import java.util.Arrays;

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
		return Arrays.stream(Row.values())
			.filter(value -> value.ordinal() == this.ordinal() + STEP)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("9 이상의 row 값은 가질수 없습니다."));
	}

	public Row minus() {
		return Arrays.stream(Row.values())
			.filter(value -> value.ordinal() == this.ordinal() - STEP)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("0 이하의 row 값은 가질수 없습니다."));
	}

	public String getName() {
		return name;
	}
}
