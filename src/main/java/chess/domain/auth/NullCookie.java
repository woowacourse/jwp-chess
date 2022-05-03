package chess.domain.auth;

import chess.domain.board.piece.Color;
import chess.exception.InvalidAccessException;
import chess.exception.InvalidStatus;

public class NullCookie extends PlayerCookie {

    public NullCookie() {
        super(null);
    }

    @Override
    public Color parsePlayerColorBy(int id) {
        throw new InvalidAccessException(InvalidStatus.COOKIE_NOT_FOUND);
    }

    @Override
    public String toString() {
        return "NullCookie{}";
    }
}
