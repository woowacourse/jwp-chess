package chess.web.service;

import chess.board.Room;
import chess.web.dao.RoomDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MockRoomDao implements RoomDao {

    private final Map<Long, Room> mockDb = new HashMap<>();

    private long sequenceId = 1;

    @Override
    public Long save(String turn, String title, String password) {
        mockDb.put(sequenceId, new Room(sequenceId, turn, title, password));
        sequenceId++;
        return sequenceId - 1;
    }

    @Override
    public Long updateTurnById(Long id, String turn) {
        Room room = mockDb.get(id);
        mockDb.put(id, new Room(id, turn, room.getTitle(), room.getPassword()));
        return id;
    }

    @Override
    public Optional<Room> findById(Long id) {
        return Optional.ofNullable(mockDb.get(id));
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(mockDb.values());
    }

    @Override
    public void delete(Long id, String password) {
        Optional<Room> optionalRoom = Optional.ofNullable(mockDb.get(id));
        if (optionalRoom.isPresent() && optionalRoom.get().getPassword().equals(password)) {
            mockDb.remove(id);
        }
    }
}
