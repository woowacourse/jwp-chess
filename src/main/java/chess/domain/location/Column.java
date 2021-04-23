package chess.domain.location;

import chess.exception.ChessException;
import chess.exception.ErrorCode;

public enum Column {
    A("a"),
    B("b"),
    C("c"),
    D("d"),
    E("e"),
    F("f"),
    G("g"),
    H("h");

    private final String value;
    private static final Column[] columns = values();

    Column(String value) {
        this.value = value;
    }

    public Column move(int value) {
        if ((this.ordinal() + value) < 0 || this.ordinal() + value >= columns.length) {
            throw new ChessException(ErrorCode.INVALID_MOVE);
        }
        return columns[(this.ordinal() + value)];
    }

    public String value() {
        return value;
    }

    public boolean canMove(int value) {
        int movedIndex = this.ordinal() + value;
        return movedIndex >= 0 && movedIndex < columns.length;
    }
}
