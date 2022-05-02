package chess.model.piece.strategy;

import chess.model.board.Square;
import chess.model.piece.strategy.move.MoveType;

public interface MovableStrategy {
    boolean movable(Square source, Square target, MoveType moveType);
}
