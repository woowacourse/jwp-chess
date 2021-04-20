package chess.domain.position;

import java.util.Objects;

import chess.domain.piece.Color;
import chess.exception.IllegalPositionException;

public class Position {

    public static final int CHESS_BOARD_START_RANGE = 0;
    public static final int CHESS_BOARD_END_RANGE = 8;
    private static final int INITIAL_BLACK_PAWN_ROW = 1;
    private static final int INITIAL_WHITE_PAWN_ROW = 6;
    private static final int CHESS_COORDINATES_LENGTH = 2;

    private final Row row;
    private final Column column;

    public Position(String chessCoordinates) {
        validateChessCoordinate(chessCoordinates);
        this.row = new Row(chessCoordinates);
        this.column = new Column(chessCoordinates);
    }

    public Position(Row row, Column column) {
        this.row = row;
        this.column = column;
    }

    public Position(int rowDegree, int columnDegree) {
        this.row = new Row(rowDegree);
        this.column = new Column(columnDegree);
    }

    private void validateChessCoordinate(String chessCoordinates) {
        if (chessCoordinates.length() != CHESS_COORDINATES_LENGTH) {
            throw new IllegalPositionException();
        }
    }

    public Position sum(Direction direction) {
        return new Position(direction.rowDegreeSum(row),
            direction.columnDegreeSum(column));
    }

    public Position difference(Position target) {
        return new Position(target.row.difference(row),
            target.column.difference(column));
    }

    public boolean isRowGreaterThanZero() {
        return row.isGreaterThanZero();
    }

    public boolean isRowLessThanZero() {
        return row.isLessThanZero();
    }

    public boolean isColumnGreaterThanZero() {
        return column.isGreaterThanZero();
    }

    public boolean isColumnLessThanZero() {
        return column.isLessThanZero();
    }

    public boolean isNotLinearPosition(Position target) {
        return !row.equals(target.row) && !column.equals(target.column);
    }

    public boolean isColumnEquals(Column column) {
        return this.column.equals(column);
    }

    public boolean isNotDiagonalPosition(Position target) {
        int rowDifference = row.abs(target.row);
        int colDifference = column.abs(target.column);

        return !(rowDifference != 0 && colDifference != 0) || rowDifference != colDifference;
    }

    public boolean isChessBoardPosition() {
        return row.isChessBoardPosition() && column.isChessBoardPosition();
    }

    public String chessCoordinate() {
        return column.chessCoordinate() + row.chessCoordinate();
    }

    public boolean isInitialPawnRow(Color color) {
        if (color.isBlack()) {
            return isInitialBlackPawnRow();
        }
        return isInitialWhitePawnRow();
    }

    public boolean isInitialBlackPawnRow() {
        return this.row.equals(new Row(INITIAL_BLACK_PAWN_ROW));
    }

    public boolean isInitialWhitePawnRow() {
        return this.row.equals(new Row(INITIAL_WHITE_PAWN_ROW));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position)o;
        return Objects.equals(row, position.row) && Objects
            .equals(column, position.column);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

}