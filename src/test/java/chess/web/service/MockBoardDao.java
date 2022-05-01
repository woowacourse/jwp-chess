package chess.web.service;

import chess.board.BoardDto;
import chess.web.dao.BoardDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MockBoardDao implements BoardDao {

    private final Map<Long, BoardDto> mockDb = new HashMap<>();

    private long sequenceId = 1;

    @Override
    public Long save(String turn, String title, String password) {
        mockDb.put(sequenceId, new BoardDto(sequenceId, turn, title, password));
        sequenceId++;
        return sequenceId - 1;
    }

    @Override
    public void updateTurnById(Long id, String turn) {
        BoardDto boardDto = mockDb.get(id);
        mockDb.put(id, new BoardDto(id, turn, boardDto.getTitle(), boardDto.getPassword()));
    }

    @Override
    public Optional<BoardDto> findById(Long id) {
        return Optional.ofNullable(mockDb.get(id));
    }

    @Override
    public List<BoardDto> findAll() {
        return new ArrayList<>(mockDb.values());
    }

    @Override
    public void delete(Long id, String password) {
        Optional<BoardDto> optionalRoom = Optional.ofNullable(mockDb.get(id));
        if (optionalRoom.isPresent() && optionalRoom.get().getPassword().equals(password)) {
            mockDb.remove(id);
        }
    }
}
