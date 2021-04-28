package chess.domain.exception;

import chess.exception.HandledException;

public class InvalidStateException extends HandledException {
    public InvalidStateException(String message) {
        super(message);
    }
}
