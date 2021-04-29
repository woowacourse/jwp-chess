package chess.domain.exception;

import chess.exception.HandledException;

public class DomainException extends HandledException {
    public DomainException(String message) {
        super(message);
    }
}
