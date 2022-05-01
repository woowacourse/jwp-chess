package chess.state;

import chess.domain.Chessboard;
import chess.domain.MovingPosition;
import chess.piece.Color;

public class Ready implements State {

    @Override
    public State start() {
        return new Play();
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
    public String getStateToString() {
        return "READY";
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean canBeDeleted() {
        return true;
    }
}
