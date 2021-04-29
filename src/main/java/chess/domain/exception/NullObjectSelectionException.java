package chess.domain.exception;

import chess.exception.HandledException;

public class NullObjectSelectionException extends HandledException {
    public NullObjectSelectionException(String message) {
        super(message);
    }
}
