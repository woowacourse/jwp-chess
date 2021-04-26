package chess.domain.position;

import chess.domain.piece.Direction;
import chess.exception.InvalidPositionException;
import java.util.Arrays;

public enum Row {
    EIGHT("8"),
    SEVEN("7"),
    SIX("6"),
    FIVE("5"),
    FOUR("4"),
    THREE("3"),
    TWO("2"),
    ONE("1");

    private final String number;

    Row(String number) {
        this.number = number;
    }

    public static Row getRow(String value) {
        return Arrays.stream(values())
            .filter(row -> row.number.equals(value))
            .findFirst()
            .orElseThrow(InvalidPositionException::new);
    }

    public Row move(Direction direction) {
        return getRow(String.valueOf(Integer.parseInt(number) + direction.rowValue()));
    }

    public boolean isInRange(int moveValue) {
        return Arrays.stream(values())
            .anyMatch(row -> row.getValue() == this.getValue() + moveValue);
    }

    public String getNumber() {
        return number;
    }

    public int getValue() {
        return Integer.parseInt(number);
    }
}
