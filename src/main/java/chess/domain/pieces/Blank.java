package chess.domain.pieces;

import chess.domain.position.Position;

public final class Blank implements Type {

    @Override
    public boolean isMovable(Position source, Position target) {
        throw new IllegalArgumentException("기물이 존재하지 않습니다.");
    }

    @Override
    public boolean isPawn() {
        return false;
    }

    @Override
    public boolean isKing() {
        return false;
    }

    @Override
    public double score() {
        return 0;
    }

    @Override
    public Symbol symbol() {
        return Symbol.BLANK;
    }

    @Override
    public boolean isBlank() {
        return true;
    }
}
