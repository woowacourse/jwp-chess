package chess.domain.state;

import chess.domain.board.Board;

public abstract class Started implements State {

    protected Board board;

    public final Board getBoard() {
        return board;
    }
}
