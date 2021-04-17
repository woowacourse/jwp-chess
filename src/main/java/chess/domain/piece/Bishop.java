package chess.domain.piece;

import java.util.Map;

import chess.domain.board.Score;
import chess.domain.position.Direction;
import chess.domain.position.Position;

public class Bishop extends Piece {

    private static final String NAME = "b";
    private static final Score SCORE = new Score(3);

    public Bishop(Color color) {
        super(NAME, color, SCORE);
    }

    @Override
    public boolean canMove(Map<Position, Piece> board, Position source, Position target) {
        if (source.isNotDiagonalPosition(target)) {
            return false;
        }
        Direction direction = Direction.diagonalTargetDirection(source.difference(target));
        do {
            source = source.sum(direction);
        } while (!source.equals(target)
            && board.get(source).isEmpty() && source.isChessBoardPosition());
        return source.equals(target);
    }

}
