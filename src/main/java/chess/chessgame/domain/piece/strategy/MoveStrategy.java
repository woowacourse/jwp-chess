package chess.chessgame.domain.piece.strategy;

import chess.chessgame.domain.order.MoveOrder;

public interface MoveStrategy {
    boolean canMove(MoveOrder moveOrder);
}
