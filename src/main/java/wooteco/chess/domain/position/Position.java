package wooteco.chess.domain.position;

import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.piece.movable.Direction;

import java.util.Objects;

public class Position {
    private static final String INVALID_INPUT_EXCEPTION_MESSAGE = "옳지 않은 좌표 입력입니다.";
    private static final int MIN_BOUND = 1;
    private static final int MAX_BOUND = 8;
    private static final int ROW_START_INDEX = 0;
    private static final int COLUMN_START_INDEX = 1;

    private final Row row;
    private final Column column;

    // package accessed
    Position(String position) {
        validate(position);
        this.row = Row.of(position.substring(ROW_START_INDEX, ROW_START_INDEX + 1));
        this.column = Column.of(position.substring(COLUMN_START_INDEX, COLUMN_START_INDEX + 1));
    }

    Position(Row row, Column column) {
        this.row = row;
        this.column = column;
    }

    private void validate(String position) {
        Objects.requireNonNull(position, INVALID_INPUT_EXCEPTION_MESSAGE);
        if (position.isEmpty()) {
            throw new IllegalArgumentException(INVALID_INPUT_EXCEPTION_MESSAGE);
        }
    }

    public Position getMovedPositionBy(Direction direction) {
        if (!checkBound(direction)) {
            return this;
        }
        Row movedRow = row.calculate(direction.getXDegree());
        Column movedColumn = column.calculate(direction.getYDegree());
        return PositionFactory.of(movedRow, movedColumn);
    }

    public boolean checkBound(Direction direction) {
        int checkingRow = row.getValue() + direction.getXDegree();
        int checkingColumn = column.getValue() + direction.getYDegree();
        return isValidBound(checkingRow) && isValidBound(checkingColumn);
    }

    public boolean isPawnInitial(Color color) {
        if (column.isWhitePawnInitial() && color.isWhite()) {
            return true;
        }
        return column.isBlackPawnInitial() && color.isBlack();
    }

    public boolean isDifferentRow(Position position) {
        return !this.row.equals(position.row);
    }

    private static boolean isValidBound(int value) {
        return value >= MIN_BOUND && value <= MAX_BOUND;
    }

    public Row getRow() {
        return row;
    }

    @Override
    public String toString() {
        return row.getName() + column.getValue();
    }
}
