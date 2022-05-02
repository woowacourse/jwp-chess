package chess.domain.state;

import chess.domain.chessboard.Board;

public abstract class Finished extends Started {

    public Finished(final Board board) {
        super(board);
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}

