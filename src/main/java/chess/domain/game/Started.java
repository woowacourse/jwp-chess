package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.game.statistics.GameState;

public abstract class Started implements Game {

    protected final Board board;

    protected Started(Board board) {
        this.board = board;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public abstract GameState getState();
}
