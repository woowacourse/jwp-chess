package chess.domain.auth;

import chess.domain.board.piece.Color;

public class NullCookie extends PlayerCookie {

    public NullCookie() {
        super(null);
    }

    @Override
    public Color parsePlayerColorBy(int id) {
        throw new IllegalArgumentException("로그인이 필요합니다.");
    }

    @Override
    public String toString() {
        return "NullCookie{}";
    }
}
