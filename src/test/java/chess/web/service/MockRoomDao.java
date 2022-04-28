package chess.web.service;

import chess.domain.board.Board;
import chess.domain.board.Team;
import chess.domain.board.Turn;
import chess.domain.board.piece.Pieces;
import chess.web.dao.RoomDao;

import java.util.*;

public class MockRoomDao implements RoomDao {
    private final Map<Long, String> mockDb = new HashMap<>();

    public MockRoomDao() {
        mockDb.put(1L, "white");
    }

    @Override
    public Optional<Turn> findTurnById(Long id) {
        return Optional.of(new Turn(Team.from(mockDb.get(id))));
    }

    @Override
    public void updateTurnById(Long id, String newTurn) {
        mockDb.put(id, newTurn);
    }

    @Override
    public Long save(String title, String password) {
        return 1L;
    }

    @Override
    public Optional<Board> findById(Long id) {
        return Optional.of(Board.create(Pieces.createInit(), Turn.init()));
    }

    @Override
    public void deleteById(Long id) {
        mockDb.remove(id);
    }

    @Override
    public List<Long> findAllId() {
        return new ArrayList<>(mockDb.keySet());
    }
}
