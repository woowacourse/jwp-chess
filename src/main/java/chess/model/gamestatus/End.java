package chess.model.gamestatus;

import chess.model.Color;
import chess.model.board.Board;
import chess.model.board.MoveResult;
import chess.model.board.Square;
import chess.model.game.GameResult;
import chess.model.piece.Piece;
import java.util.Map;

public class End implements Status {

    private final Board board;

    public End(final Board board) {
        this.board = board;
    }

    @Override
    public Status start() {
        return Playing.init();
    }

    @Override
    public MoveResult move(final Square from, final Square to, final Color turn) {
        throw new IllegalArgumentException("게임이 종료되었으면 말을 이동할 수 없습니다.");
    }

    @Override
    public GameResult getResult() {
        return board.getResult();
    }

    @Override
    public Status end() {
        return this;
    }

    @Override
    public boolean isEnd() {
        return true;
    }

    @Override
    public Map<Square, Piece> getBoard() {
        return Map.copyOf(board.getBoard());
    }

    @Override
    public boolean isPlaying() {
        return false;
    }
}
