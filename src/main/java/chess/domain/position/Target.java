package chess.domain.position;

import chess.domain.state.State;
import chess.exception.ChessException;
import chess.exception.ErrorInformation;

public class Target {
    private final Position position;

    private Target(final Position position) {
        this.position = position;
    }

    public static Target valueOf(final Source source, final Position target, final State state) {
        validateTarget(source, target, state);
        return new Target(target);
    }

    private static void validateTarget(final Source source, final Position target, final State state) {
        if (state.findPiece(target).isPresent()) {
            throw new ChessException(ErrorInformation.INVALID_TARGET);
        }
        if (source.isSamePosition(target)) {
            throw new ChessException(ErrorInformation.SAME_LOCATION);
        }
    }

    public Position getPosition() {
        return position;
    }
}
