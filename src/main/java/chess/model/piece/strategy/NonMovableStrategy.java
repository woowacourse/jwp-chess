package chess.model.piece.strategy;

import chess.model.board.Square;
import chess.model.piece.strategy.move.MoveType;

public class NonMovableStrategy implements MovableStrategy {
    @Override
    public boolean movable(Square source, Square target, MoveType moveType) {
        return false;
    }
}
