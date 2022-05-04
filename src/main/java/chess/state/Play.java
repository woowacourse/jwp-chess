package chess.state;

import chess.domain.Chessboard;
import chess.domain.MovingPosition;
import chess.piece.Color;


public class Play implements State {

    @Override
    public State start() {
        throw new UnsupportedOperationException(UNSUPPORTED_STATE);
    }

    @Override
    public State move(Chessboard chessboard, MovingPosition movingPosition, Color turn) {
        chessboard.move(movingPosition, turn);

        if (chessboard.isOver()) {
            return new Finish();
        }

        return new Play();
    }

    @Override
    public State end() {
        return new Finish();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public String getStateToString() {
        return "PLAY";
    }

    @Override
    public boolean canBeDeleted() {
        return false;
    }

}
