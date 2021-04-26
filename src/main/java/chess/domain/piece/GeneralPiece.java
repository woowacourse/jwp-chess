package chess.domain.piece;

import chess.domain.piece.direction.MoveStrategies;
import chess.domain.position.Position;
import chess.domain.position.Source;
import chess.domain.position.Target;

import java.util.List;

public abstract class GeneralPiece extends Piece {

    private final MoveStrategies moveStrategies;

    public GeneralPiece(final Color color, final String initialName, final Position position) {
        super(color, initialName, position);
        this.moveStrategies = assignMoveStrategies();
    }

    @Override
    public boolean canMove(final Target target) {
        return isPossibleDirection(target) && (isOpponent(target) || target.isBlank());
    }

    private boolean isPossibleDirection(final Target target) {
        final List<Integer> result = target.subtract(new Source(this));
        return moveStrategies.strategies().stream()
                .anyMatch(moveStrategy -> moveStrategy.isSameDirection(result.get(0), result.get(1)));
    }

    @Override
    public boolean isPawn() {
        return false;
    }

    @Override
    public boolean isBlank() {
        return false;
    }

    @Override
    public boolean isKing() {
        return false;
    }

    protected abstract MoveStrategies assignMoveStrategies();
}
