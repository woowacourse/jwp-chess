package chess.chessgame.domain.room.game.board.piece.strategy;

import chess.chessgame.domain.room.game.board.order.MoveOrder;

public interface MoveStrategy {
    boolean canMove(MoveOrder moveOrder);
}
