package chess.model.gamestatus;

import chess.model.Color;
import chess.model.board.Board;
import chess.model.board.EmptyBoardInitializer;
import chess.model.board.MoveResult;
import chess.model.board.Square;
import chess.model.game.GameResult;
import chess.model.piece.Piece;
import java.util.Map;

public class Ready implements Status {
    private final Board board;

    public Ready() {
        this.board = new Board(new EmptyBoardInitializer());
    }

    @Override
    public Status start() {
        return Playing.init();
    }

    @Override
    public MoveResult move(Square from, Square to, Color turn) {
        throw new IllegalArgumentException("게임이 시작되지 않았으면 움직일 수 없습니다.");
    }

    @Override
    public GameResult getResult() {
        throw new IllegalArgumentException("게임이 시작되지 않았으면 결과를 조회할 수 없습니다.");
    }

    @Override
    public Status end() {
        throw new IllegalArgumentException("게임이 시작되지 않았으면 게임을 종료할 수 없습니다.");
    }

    @Override
    public boolean isEnd() {
        return false;
    }

    @Override
    public Map<Square, Piece> getBoard() {
        return Map.copyOf(board.getBoard());
    }
}
