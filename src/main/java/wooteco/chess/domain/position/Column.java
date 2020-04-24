package wooteco.chess.domain.position;

import java.util.Arrays;

public enum Column {
	A("a"),
	B("b"),
	C("c"),
	D("d"),
	E("e"),
	F("f"),
	G("g"),
	H("h");

	private static final int STEP = 1;

	private final String name;

	Column(String name) {
		this.name = name;
	}

	public Column plus() {
		return Arrays.stream(Column.values())
			.filter(value -> value.ordinal() == this.ordinal() + STEP)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("9 이상의 column 값은 가질수 없습니다."));
	}

	public Column minus() {
		return Arrays.stream(Column.values())
			.filter(value -> value.ordinal() == this.ordinal() - STEP)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("0 이하의 column 값은 가질수 없습니다."));
	}

	public String getName() {
		return name;
	}
}
