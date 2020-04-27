package wooteco.chess.domain.piece.strategy;

import wooteco.chess.domain.Color;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Direction;
import wooteco.chess.domain.piece.Path;
import wooteco.chess.domain.piece.Piece;

import java.util.List;
import java.util.Map;

public class PawnMoveStrategy implements MoveStrategy {
    List<Direction> moveDirections;
    List<Direction> attackDirections;

    public PawnMoveStrategy(Color color) {
        createMoveDirections(color);
        createAttackDirections(color);
    }

    private void createMoveDirections(Color color) {
        if (color == Color.WHITE) {
            moveDirections = Direction.whitePawnGoDirection();
        }

        if (color == Color.BLACK) {
            moveDirections = Direction.blackPawnGoDirection();
        }
    }

    private void createAttackDirections(Color color) {
        if (color == Color.WHITE) {
            attackDirections = Direction.whitePawnCatchDirection();
        }

        if (color == Color.BLACK) {
            attackDirections = Direction.blackPawnCatchDirection();
        }
    }

    @Override
    public Path findMovablePositions(Path path, Map<Position, Piece> pieces) {
        path.findPathPawnByDirections(moveDirections, attackDirections, pieces);
        return path;
    }
}
