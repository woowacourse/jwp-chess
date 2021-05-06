package chess.domain.state;

import chess.domain.piece.Pieces;
import chess.domain.position.Source;
import chess.domain.position.Target;
import chess.exception.ChessException;
import chess.exception.ErrorInformation;

public class FinishedTurn extends Turn {
    public FinishedTurn(final Pieces pieces) {
        super(pieces);
    }

    @Override
    public final State move(final Source source, final Target target, final State anotherState) {
        throw new ChessException(ErrorInformation.INVALID_TURN);
    }

    @Override
    public final boolean isFinishedTurn() {
        return true;
    }

    @Override
    public State toRunningTurn(final State anotherState) {
        if (anotherState.isFinishedTurn()) {
            return new RunningTurn(pieces());
        }
        throw new ChessException(ErrorInformation.INVALID_TURN);
    }
}
