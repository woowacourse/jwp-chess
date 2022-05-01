package chess.state;

import chess.domain.Chessboard;
import chess.domain.MovingPosition;
import chess.piece.Color;

public class Finish implements State {

    @Override
    public State start() {
        throw new UnsupportedOperationException(UNSUPPORTED_STATE);
    }

    @Override
    public State move(Chessboard chessboard, MovingPosition movingPosition, Color turn) {
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

    @Override
    public boolean canBeDeleted() {
        return true;
    }

}
