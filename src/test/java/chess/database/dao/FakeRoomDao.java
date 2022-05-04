package chess.database.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import chess.database.entity.RoomEntity;

public class FakeRoomDao implements RoomDao {

    private static class FakeRow {
        private final String roomName;
        private final String password;

        public FakeRow(String roomName, String password) {
            this.roomName = roomName;
            this.password = password;
        }

        public String getRoomName() {
            return roomName;
        }

        public String getPassword() {
            return password;
        }
    }

    private Long id;
    private Map<Long, FakeRow> memoryDatabase;

    public FakeRoomDao() {
        this.id = 1L;
        this.memoryDatabase = new HashMap<>();
    }

    @Override
    public Long saveRoom(RoomEntity entity) {
        memoryDatabase.put(id, new FakeRow(entity.getRoomName(), entity.getPassword()));
        return id++;
    }

    @Override
    public Optional<RoomEntity> findRoomById(Long id) {
        final FakeRow fakeRow = memoryDatabase.get(id);
        return Optional.of(RoomEntity.from(fakeRow.getRoomName(), fakeRow.getPassword()));
    }

    @Override
    public List<RoomEntity> findAll() {
        return memoryDatabase.entrySet()
            .stream()
            .map(entry -> new RoomEntity(
                entry.getKey(),
                entry.getValue().getRoomName(),
                entry.getValue().getPassword()
            ))
            .collect(Collectors.toList());
    }

    @Override
    public void deleteRoom(Long roomId) {
        memoryDatabase.remove(roomId);
    }
}
