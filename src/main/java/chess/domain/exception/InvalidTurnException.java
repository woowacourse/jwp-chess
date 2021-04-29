package chess.domain.exception;

import chess.exception.HandledException;

public class InvalidTurnException extends HandledException {
    public InvalidTurnException(String message) {
        super(message);
    }
}
