package chess.domain.position.exception;

import chess.exception.chessgame.ChessException;

public class PositionException extends ChessException {

    public PositionException(String message) {
        super(message);
    }
}
