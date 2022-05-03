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
        return new Board(fakeAutoIncrementId, new Running(), Team.WHITE);
    }

    @Override
    public int deleteByRoomId(int id) {
        return fakeAutoIncrementId;
    }

    @Override
    public Board getById(int id) {
        return new Board(fakeAutoIncrementId, new Running(), Team.WHITE);
    }

    @Override
    public int updateStatus(int boardId, Status status) {
        throw new UnsupportedOperationException("테스트에서 사용하지 않는 메서드입니다.");
    }

    @Override
    public int updateTeamById(int boardId, Team team) {
        throw new UnsupportedOperationException("테스트에서 사용하지 않는 메서드입니다.");
    }

    @Override
    public Status getStatusById(int boardId) {
        return new Running();
    }

    @Override
    public Team getTeamById(int roomId) {
        return Team.WHITE;
    }
}
