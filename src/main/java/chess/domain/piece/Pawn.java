package chess.domain.piece;

import chess.domain.piece.direction.MoveStrategies;
import chess.domain.position.Position;
import chess.domain.position.Target;
import java.util.List;

public final class Pawn extends Piece {

    private static final String INITIAL_NAME = "P";
    private final MoveStrategies moveStrategies;

    public Pawn(final Color color, final Position position) {
        super(color, INITIAL_NAME, position);
        this.moveStrategies = MoveStrategies.pawnMoveStrategies();
    }

//    @Override
//    public boolean canMove(final Target target) {
//        final List<Integer> result = subtractByTeam(source, target);
//        final Difference difference = new Difference(result);
//        final Direction direction = POSSIBLE_DIRECTIONS.stream()
//            .filter(possibleDirection -> possibleDirection.isSameDirection(difference))
//            .findAny()
//            .orElse(Direction.NOTHING);
//
//        return direction != Direction.NOTHING && checkPossible(direction, piece, source.vertical());
//    }
//
//    private List<Integer> subtractByTeam(final Target target) {
//        if (team().isBlackTeam()) {
//            return source.subtract(target);
//        }
//        return target.subtract(source);
//    }
//
//    private boolean checkPossible(final Direction direction, final Piece piece, final Vertical vertical) {
//        if (direction.isNorth()) {
//            return piece.isBlank();
//        }
//        if (direction.isNorthWest() || direction.isNorthEast()) {
//            return piece.isOpponent(this);
//        }
//        if (direction.isInitialPawnNorth()) {
//            return INITIAL_VERTICALS.contains(vertical.value()) && piece.isBlank();
//        }
//        return false;
//    }

    @Override
    public boolean canMove(Target target) {
        return false;
    }

    @Override
    public boolean isPawn() {
        return true;
    }

    @Override
    public boolean isBlank() {
        return false;
    }

    @Override
    public boolean isKing() {
        return false;
    }
}
