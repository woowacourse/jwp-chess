package chess.web.service;

import chess.board.Room;
import chess.web.dao.RoomDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

public class MockRoomDao implements RoomDao {

    private final Map<Long, Room> mockDb = new HashMap<>();

    private long index = 1;

    @Override
    public Long save(Long boardId, String title, String password) {
        long key = index;
        mockDb.put(index++, new Room(boardId.intValue(), title, password));
        return key;
    }

    @Override
    public Optional<Room> findByBoardId(Long boardId) {
        return mockDb.values().stream()
                .filter(room -> room.getBoardId() == boardId)
                .findFirst();
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(mockDb.values());
    }

    @Override
    public void delete(Long boardId, String password) {
        Optional<Long> key = mockDb.entrySet().stream()
                .filter(entry -> entry.getValue().getBoardId() == boardId)
                .filter(entry -> entry.getValue().getPassword().equals(password))
                .map(Entry::getKey)
                .findFirst();

        key.ifPresent(mockDb::remove);
    }
}
