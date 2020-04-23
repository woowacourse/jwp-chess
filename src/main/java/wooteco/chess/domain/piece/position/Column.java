package wooteco.chess.domain.piece.position;

public enum Column {
	A("a", 1),
	B("b", 2),
	C("c", 3),
	D("d", 4),
	E("e", 5),
	F("f", 6),
	G("g", 7),
	H("h", 8);

	private String columnName;
	private int number;

	Column(String columnName, int number) {
		this.columnName = columnName;
		this.number = number;
	}

	public int gap(Column column){
		return column.number - this.number;
	}

	public String getColumnName() {
		return columnName;
	}

	public int getNumber() {
		return number;
	}
}
