package chess.chessgame.domain.room.game.board.piece.strategy;

import chess.chessgame.domain.room.game.board.order.MoveOrder;
import chess.chessgame.domain.room.game.board.position.Direction;

public class KingMoveStrategy extends DefaultMoveStrategy {
    public KingMoveStrategy() {
        super(Direction.everyDirection());
    }

    @Override
    public boolean canMove(MoveOrder moveOrder) {
        if (moveOrder.getRoute().size() != 0) {
            throw new IllegalArgumentException("킹이 움직일 수 있는 범위를 벗어났습니다.");
        }
        return super.canMove(moveOrder);
    }
}
