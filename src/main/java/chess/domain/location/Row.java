package chess.domain.location;

import chess.exception.ChessException;
import chess.exception.ErrorCode;

public enum Row {
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8");

    private final String value;
    private static final Row[] rows = values();

    Row(String value) {
        this.value = value;
    }

    public Row move(int value) {
        if ((this.ordinal() + value) < 0 || this.ordinal() + value >= rows.length) {
            throw new ChessException(ErrorCode.INVALID_MOVE);
        }
        return rows[(this.ordinal() + value)];
    }

    public String value() {
        return value;
    }

    public boolean canMove(int value) {
        int movedIndex = this.ordinal() + value;
        return movedIndex >= 0 && movedIndex < rows.length;
    }
}
