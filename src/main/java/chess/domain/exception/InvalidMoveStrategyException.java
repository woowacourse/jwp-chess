package chess.domain.exception;

import chess.exception.HandledException;

public class InvalidMoveStrategyException extends HandledException {
    public InvalidMoveStrategyException(String message) {
        super(message);
    }
}
