package wooteco.chess.domain.position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Column {

	private static final Map<Integer, Column> COLUMN_CACHE = new HashMap<>();

	static {
		COLUMN_CACHE.put(1, new Column(1, "A"));
		COLUMN_CACHE.put(2, new Column(2, "B"));
		COLUMN_CACHE.put(3, new Column(3, "C"));
		COLUMN_CACHE.put(4, new Column(4, "D"));
		COLUMN_CACHE.put(5, new Column(5, "E"));
		COLUMN_CACHE.put(6, new Column(6, "F"));
		COLUMN_CACHE.put(7, new Column(7, "G"));
		COLUMN_CACHE.put(8, new Column(8, "H"));
	}

	private final int value;
	private final String name;

	private Column(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public static Column of(String value) {
		return COLUMN_CACHE.values()
			.stream()
			.filter(column -> column.isSameName(value))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Row 값입니다."));
	}

	public static Column of(int value) {
		return COLUMN_CACHE.get(value);
	}

	public static List<Column> columnNames() {
		return new ArrayList<>(COLUMN_CACHE.values());
	}

	private boolean isSameName(String value) {
		if (Objects.isNull(value)) {
			throw new IllegalArgumentException("입력값이 null입니다.");
		}
		return name.equals(value.toUpperCase());
	}

	public String getName() {
		return name;
	}

	public Column nextColumn(int columnDirection) {
		return COLUMN_CACHE.get(value + columnDirection);
	}

	@Override
	public String toString() {
		return "Column{" +
			"name='" + name + '\'' +
			'}';
	}
}
