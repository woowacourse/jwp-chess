package chess.domain.piece;

import java.util.Objects;

public class Position {

    private final int row;
    private final int column;

    public Position(String str) {
        this(makeRow(str), makeColumn(str));
    }

    public Position(final int row, final int column) {
        this.row = row;
        this.column = column;
    }

    private static int makeColumn(String str) {
        return str.split("")[0].toCharArray()[0] - 'a';
    }

    private static int makeRow(String str) {
        return Integer.parseInt(str.split("")[1]) - 1;
    }

    public double calculateGradient(Position position) {
        return (position.row - row) / (double) (position.column - column);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
