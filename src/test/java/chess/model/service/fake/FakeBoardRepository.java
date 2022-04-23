package chess.model.service.fake;

import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.piece.Team;
import chess.model.square.Square;
import chess.model.status.Running;
import chess.model.status.Status;
import chess.repository.BoardRepository;

import java.util.Map;

public class FakeBoardRepository implements BoardRepository<Board> {

    private int fakeAutoIncrementId;

    public FakeBoardRepository(int fakeAutoIncrementId) {
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
    public int updateStatus(int boardId, Status status) {
        return 0;
    }

    @Override
    public int updateTeamById(int boardId, Team team) {
        return 0;
    }

    @Override
    public Status getStatusById(int boardId) {
        return null;
    }
}
