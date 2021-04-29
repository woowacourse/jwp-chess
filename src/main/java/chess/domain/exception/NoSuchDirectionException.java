package chess.domain.exception;

import chess.exception.HandledException;

public class NoSuchDirectionException extends HandledException {
    public NoSuchDirectionException(String message) {
        super(message);
    }
}
