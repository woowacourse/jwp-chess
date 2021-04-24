package chess.domain.piece.strategy;

import chess.domain.order.MoveRoute;
import chess.domain.position.Direction;
import chess.exception.InvalidMoveStrategyException;

import java.util.List;
import java.util.Objects;

public abstract class DefaultMoveStrategy implements MoveStrategy {
    private final List<Direction> movableDirections;

    public DefaultMoveStrategy(List<Direction> movableDirections) {
        this.movableDirections = movableDirections;
    }

    @Override
    public boolean canMove(MoveRoute moveRoute) {
        validateProperDirection(moveRoute);
        validateRouteIsNotBlocked(moveRoute);
        validateToSquareHasSameColorOfPiece(moveRoute);

        return true;
    }

    private void validateProperDirection(MoveRoute moveRoute) {
        if (!movableDirections.contains(moveRoute.getDirection())) {
            throw new InvalidMoveStrategyException("움직일 수 없는 방향입니다.");
        }
    }

    private void validateRouteIsNotBlocked(MoveRoute route) {
        if (route.isBlocked()) {
            throw new InvalidMoveStrategyException("중간에 말이 있어 행마할 수 없습니다.");
        }
    }

    private void validateToSquareHasSameColorOfPiece(MoveRoute moveRoute) {
        if (moveRoute.hasPieceAtToPosition()) {
            validateSameColorPiece(moveRoute);
        }
    }

    private void validateSameColorPiece(MoveRoute moveRoute) {
        if (moveRoute.getPieceAtToPosition().isSameColor(moveRoute.getPieceAtFromPosition())) {
            throw new InvalidMoveStrategyException("동일한 진영의 말이 있어서 행마할 수 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        return this.getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash(movableDirections);
    }
}
