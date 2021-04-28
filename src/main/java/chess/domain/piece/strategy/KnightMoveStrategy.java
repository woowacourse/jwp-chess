package chess.domain.piece.strategy;

import chess.domain.exception.InvalidMoveStrategyException;
import chess.domain.order.MoveRoute;
import chess.domain.position.Direction;

public class KnightMoveStrategy extends DefaultMoveStrategy {
    public KnightMoveStrategy() {
        super(Direction.knightDirection());
    }

    @Override
    public boolean canMove(MoveRoute moveRoute) {
        super.canMove(moveRoute);
        if (moveRoute.length() > 2) {
            throw new InvalidMoveStrategyException("나이트가 움직일 수 있는 범위를 넘어섰습니다.");
        }
        return true;
    }
}
