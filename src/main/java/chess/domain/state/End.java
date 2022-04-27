package chess.domain.state;

import chess.domain.chessboard.Board;

import static chess.domain.state.Command.*;

public final class End extends Finished {

    End(Board board) {
        super(board);
    }

    @Override
    public State proceed(final String command) {
        if (STATUS.isUserInput(command)) {
            return new Status(board);
        }
        return this;
    }

    @Override
    public boolean isStatus() {
        return false;
    }
}
