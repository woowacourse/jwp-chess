package chess.model.service.fake;

import chess.model.board.Board;
import chess.model.piece.Team;
import chess.model.status.Running;
import chess.model.status.Status;
import chess.repository.BoardRepository;

public class FakeBoardRepository implements BoardRepository<Board> {

    private int fakeAutoIncrementId;

    public FakeBoardRepository(int fakeAutoIncrementId) {
        this.fakeAutoIncrementId = fakeAutoIncrementId;
    }

    @Override
    public Board save(Board board) {
        return new Board(1, new Running(), Team.WHITE);
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

    @Override
    public Team getTeamById(int roomId) {
        return null;
    }
}
