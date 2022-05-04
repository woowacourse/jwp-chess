package chess.domain.state;

import chess.domain.ChessBoard;
import chess.domain.position.Position;
import chess.exception.InvalidChessStateException;

public abstract class Finished extends Started {

    protected Finished(ChessBoard chessBoard) {
        super(chessBoard);
    }

    @Override
    public State start() {
        chessBoard.init();
        return new WhiteTurn(chessBoard);
    }

    @Override
    public final State end() {
        throw new InvalidChessStateException(INVALID_CHESS_STATE_EXCEPTION);
    }

    @Override
    public final State move(Position source, Position target) {
        throw new InvalidChessStateException(INVALID_CHESS_STATE_EXCEPTION);
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public final boolean isFinished() {
        return true;
    }
}
