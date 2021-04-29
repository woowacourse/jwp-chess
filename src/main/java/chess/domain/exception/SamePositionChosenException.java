package chess.domain.exception;

import chess.exception.HandledException;

public class SamePositionChosenException extends HandledException {
    public SamePositionChosenException(String message) {
        super(message);
    }
}
