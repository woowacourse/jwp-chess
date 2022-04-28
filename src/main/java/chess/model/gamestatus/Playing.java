package chess.model.gamestatus;

import chess.model.Color;
import chess.model.board.Board;
import chess.model.board.MoveResult;
import chess.model.board.Square;
import chess.model.game.GameResult;
import chess.model.piece.Piece;
import java.util.Map;

public class Playing implements Status {

    private final Board board;

    public Playing(final Board board) {
        this.board = board;
    }

    public static Status init() {
        return new Playing(Board.init());
    }

    @Override
    public Status start() {
        return init();
    }

    @Override
    public MoveResult move(final Square from, final Square to, final Color turn) {
        if (!isValidTurn(from, turn)) {
            throw new IllegalArgumentException(
                    String.format("해당 기물을 움직일 권한이 없습니다. 현재 %s의 차례입니다.", turn.name()));
        }
        return board.move(from, to);
    }

    private boolean isValidTurn(Square from, Color turn) {
        return board.findPieceBySquare(from).isSameColor(turn);
    }

    @Override
    public GameResult getResult() {
        return board.getResult();
    }

    @Override
    public Status end() {
        return new End(board);
    }

    @Override
    public boolean isEnd() {
        return false;
    }

    @Override
    public Map<Square, Piece> getBoard() {
        return Map.copyOf(board.getBoard());
    }

    @Override
    public boolean isPlaying() {
        return true;
    }
}
