package chess.domain.state;

import chess.domain.piece.Pieces;
import chess.domain.position.Source;
import chess.domain.position.Target;
import chess.exception.RunningStateException;

public class Running extends Turn {

    public Running(final Pieces pieces) {
        super(pieces);
    }

    @Override
    public State move(final Source source, final Target target) {
        final Pieces pieces = pieces();
        pieces.move(source, target);
        return new Finished(pieces);
    }

    @Override
    public boolean isFinish() {
        return false;
    }

    @Override
    public State toRunningState(final State anotherState) {
        throw new RunningStateException();
    }
}
