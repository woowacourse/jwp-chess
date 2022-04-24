package chess.model.gamestatus;

import chess.model.Color;
import chess.model.game.GameResult;
import chess.model.board.MoveResult;
import chess.model.board.Score;
import chess.model.board.Board;
import chess.model.board.Square;
import chess.model.piece.Piece;
import java.util.Map;

public class End implements Status {
    private final Board board;

    public End(Board board) {
        this.board = board;
    }

    @Override
    public Status start() {
        return Playing.init();
    }

    @Override
    public MoveResult move(Square from, Square to, Color turn) {
        throw new IllegalArgumentException("게임이 종료되었으면 말을 이동할 수 없습니다.");
    }

    @Override
    public Score getScore() {
        return board.calculateScore();
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
        return board.getBoard();
    }
}
