package chess.web.service;

import chess.board.Team;
import chess.board.Turn;
import chess.web.dao.BoardDao;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MockBoardDao implements BoardDao {
    private final Map<Long, String> mockDb = new HashMap<>();

    private long sequenceId = 1L;

    public MockBoardDao() {
    }

    @Override
    public Long save(Turn turn) {
        mockDb.put(sequenceId++, turn.getTeam().value());
        return sequenceId - 1;
    }

    @Override
    public Optional<Turn> findTurnById(Long id) {
        return Optional.of(new Turn(Team.from(mockDb.get(id))));
    }

    @Override
    public Long update(Long boardId, Turn turn) {
        mockDb.put(boardId, turn.getTeam().value());
        return boardId;
    }

    @Override
    public void deleteById(Long id) {
        mockDb.remove(id);
    }
}
