package chess.domain.strategy;

import chess.domain.ChessBoard;
import chess.domain.position.Direction;
import chess.domain.position.Position;
import chess.exception.IllegalMoveException;

public final class KnightMoveStrategy implements MoveStrategy {

    @Override
    public void isMovable(Position source, Position target, ChessBoard chessBoard) {
        if (source.equals(target)) {
            throw new IllegalMoveException("제자리에 머무를 수 없습니다.");
        }

        checkKnightMoving(source, target);
    }

    private void checkKnightMoving(Position source, Position target) {
        Direction direction = Direction.of(source, target);

        if (!direction.isKnightDirection()) {
            throw new IllegalMoveException("잘못된 방향입니다.");
        }
    }
}
