package chess.domain.position;

import chess.domain.util.Direction;
import chess.exception.InvalidPositionException;
import chess.exception.OutOfBoardRangeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Position {
    private static final int ASCII_GAP = 96;
    public static final int END_INDEX = 8;
    public static final int START_INDEX = 1;
    public static final int ROW_SIZE = 8;

    private final int x;
    private final int y;

    public Position(final int x, final int y) {
        if (!isInBoardRange(x, y)) {
            throw new OutOfBoardRangeException();
        }
        this.x = x;
        this.y = y;
    }

    public static Position of(final String position) {
        if (position.length() != 2) {
            throw new InvalidPositionException(position);
        }
        return of(String.valueOf(position.charAt(0) - ASCII_GAP),
                String.valueOf(position.charAt(1)));
    }

    public static Position of(final String x, final String y) {
        return new Position(Integer.parseInt(x), Integer.parseInt(y));
    }

    public static List<Position> getAllPositions() {
        List<Position> positions = new ArrayList<>();
        for (int row = START_INDEX; row <= END_INDEX; row++) {
            for (int col = START_INDEX; col <= END_INDEX; col++) {
                positions.add(new Position(col, row));
            }
        }
        return positions;
    }

    public Position moveBy(final Direction direction) {
        return new Position(x + direction.getColumn(), y + direction.getRow());
    }

    public static boolean isInBoardRange(final int x, final int y) {
        return x <= END_INDEX && x >= START_INDEX &&
                y <= END_INDEX && y >= START_INDEX;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
        return Objects.equals(x, position.x) &&
                Objects.equals(y, position.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.valueOf((char)(x + 96)) + String.valueOf(y);
    }
}
