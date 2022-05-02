package chess.domain.state;

import chess.domain.chessboard.Board;
import chess.domain.chessboard.position.Position;
import chess.domain.piece.Piece;
import chess.domain.game.Player;

import java.util.Map;

public abstract class Started implements State {

    protected final Board board;

    protected Started(final Board board) {
        this.board = board;
    }

    @Override
    public Map<Position, Piece> getBoard() {
        return board.getBoard();
    }

    @Override
    public Player getNextTurnPlayer() {
        return Player.NONE;
    }
}
