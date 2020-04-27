package wooteco.chess.domain.move;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.move.direction.Direction;

import java.util.List;

public abstract class MoveStrategy {
    public boolean movable(Position source, Position target, Board board) {
        if (isSamePosition(source, target)) {
            return false;
        }

        return checkMovement(source, target, board);
    }

    private boolean isSamePosition(Position source, Position target) {
        return source.equals(target);
    }

    protected boolean checkObstacle(Position source, Position target, Board board) {
        Direction direction = Direction.findDirection(source, target);
        List<Position> path = direction.findPath(source, target);

        return path.stream()
                .allMatch(board::isEmpty);
    }

    protected boolean checkTarget(Position source, Position target, Board board) {
        Piece sourcePiece = board.getPiece(source);
        if (board.isEmpty(target)) {
            return true;
        }
        Piece targetPiece = board.getPiece(target);
        return sourcePiece.isEnemy(targetPiece);
    }

    public abstract boolean checkMovement(Position source, Position target, Board board);
}
