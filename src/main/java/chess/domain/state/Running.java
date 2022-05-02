package chess.domain.state;

import chess.domain.ChessBoard;
import chess.domain.Result;
import chess.exception.InvalidChessStateException;

public abstract class Running extends Started {

    protected Running(ChessBoard chessBoard) {
        super(chessBoard);
    }

    @Override
    public final State start() {
        throw new InvalidChessStateException(INVALID_CHESS_STATE_EXCEPTION);
    }

    @Override
    public final State end() {
        return new End(chessBoard);
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public final boolean isFinished() {
        return false;
    }

    @Override
    public final Result winner() {
        throw new InvalidChessStateException(INVALID_CHESS_STATE_EXCEPTION);
    }
}
