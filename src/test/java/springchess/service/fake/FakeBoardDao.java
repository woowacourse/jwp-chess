package springchess.service.fake;

import chess.dao.BoardDao;
import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.piece.Team;
import chess.model.square.Square;
import chess.model.status.Running;

import java.util.Map;

public class FakeBoardDao implements BoardDao<Board> {

    private int fakeAutoIncrementId;

    public FakeBoardDao(int fakeAutoIncrementId) {
        this.fakeAutoIncrementId = fakeAutoIncrementId;
    }

    @Override
    public Board save(Board board) {
        return null;
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    @Override
    public int deleteById(int id) {
        return 0;
    }

    @Override
    public Board getById(int id) {
        return new Board(fakeAutoIncrementId, new Running(), Team.WHITE);
    }

    @Override
    public Board init(Board board, Map<Square, Piece> startingPieces) {
        return new Board(101, board.getStatus(), Team.WHITE);
    }

    @Override
    public int finishGame(int boardId) {
        return 0;
    }

    @Override
    public boolean isEnd(int boardId) {
        return false;
    }

    @Override
    public int changeTurn(int boardId) {
        return 0;
    }
}
