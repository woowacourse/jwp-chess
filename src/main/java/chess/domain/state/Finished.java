package chess.domain.state;

import chess.domain.piece.Pieces;
import chess.domain.position.Source;
import chess.domain.position.Target;
import chess.exception.FinishException;
import chess.exception.ValidTurnException;

public class Finished extends Turn {

    public Finished(final Pieces pieces) {
        super(pieces);
    }

    @Override
    public final State move(final Source source, final Target target) {
        throw new FinishException();
    }

    @Override
    public final boolean isFinish() {
        return true;
    }

    @Override
    public State toRunningState(final State anotherState) {
        if (anotherState.isFinish()) {
            return new Running(pieces());
        }
        throw new ValidTurnException();
    }
}
