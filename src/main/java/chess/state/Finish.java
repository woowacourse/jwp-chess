package chess.state;

import chess.domain.Chessboard;
import chess.domain.MovingPosition;
import chess.domain.Turn;

public class Finish implements State {

    @Override
    public State start() {
        throw new UnsupportedOperationException(UNSUPPORTED_STATE);
    }

    @Override
    public State move(Chessboard chessboard, MovingPosition movingPosition, Turn turn) {
        throw new UnsupportedOperationException(UNSUPPORTED_STATE);
    }

    @Override
    public State end() {
        throw new UnsupportedOperationException(UNSUPPORTED_STATE);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public String getStateToString() {
        return "FINISH";
    }

}
