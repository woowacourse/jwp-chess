package chess.domain.state;

import chess.domain.piece.Pieces;
import chess.domain.position.Source;
import chess.domain.position.Target;

public class Finished extends Turn {
    public Finished(final Pieces pieces) {
        super(pieces);
    }

    @Override
    public final State move(final Source source, final Target target) {
        throw new UnsupportedOperationException("끝난 상태에서는 기물을 움직일 수 없습니다.");
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
        throw new IllegalStateException("상태방 차례입니다.");
    }
}
