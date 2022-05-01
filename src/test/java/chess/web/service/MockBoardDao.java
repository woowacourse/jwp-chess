package chess.web.service;

import chess.board.BoardEntity;
import chess.web.dao.BoardDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MockBoardDao implements BoardDao {

    private final Map<Long, MockBoard> mockDb = new HashMap<>();

    private long sequenceId = 1;

    @Override
    public Long save(String turn, String title, String password) {
        mockDb.put(sequenceId, new MockBoard(turn, title, password));
        sequenceId++;
        return sequenceId - 1;
    }

    @Override
    public void updateTurnById(Long id, String turn) {
        MockBoard board = mockDb.get(id);
        mockDb.put(id, new MockBoard(turn, board.title, board.password));
    }

    @Override
    public Optional<BoardEntity> findById(Long id) {
        MockBoard mockBoard = mockDb.get(id);
        return Optional.of(new BoardEntity(id, mockBoard.turn, mockBoard.title, mockBoard.password));
    }

    @Override
    public List<BoardEntity> findAll() {
        return mockDb.keySet().stream()
                .map(id -> {
                    MockBoard mockBoard = mockDb.get(id);
                    return new BoardEntity(id, mockBoard.turn, mockBoard.title, mockBoard.password);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id, String password) {
        Optional<MockBoard> board = Optional.ofNullable(mockDb.get(id));
        if (board.isPresent() && board.get().password.equals(password)) {
            mockDb.remove(id);
        }
    }

    private static class MockBoard {
        private final String turn;
        private final String title;
        private final String password;

        public MockBoard(String turn, String title, String password) {
            this.turn = turn;
            this.title = title;
            this.password = password;
        }
    }
}
