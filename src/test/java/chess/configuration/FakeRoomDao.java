package chess.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import chess.repository.entity.RoomEntity;
import chess.repository.RoomDao;

public class FakeRoomDao implements RoomDao {

    private int autoIncrementId = 0;
    private final Map<Integer, RoomEntity> database = new HashMap<>();

    @Override
    public int save(RoomEntity room) {
        autoIncrementId++;
        database.put(autoIncrementId,
            new RoomEntity(autoIncrementId, room));
        return autoIncrementId;
    }

    @Override
    public Optional<RoomEntity> findByName(String name) {
        return database.keySet().stream()
            .filter(key -> database.get(key).getName().equals(name))
            .map(key -> new RoomEntity(
                key, new RoomEntity(database.get(key).getName(), database.get(key).getPassword())))
            .findAny();
    }

    @Override
    public Optional<RoomEntity> findById(int roomId) {
        return Optional.ofNullable(database.get(roomId));
    }

    @Override
    public void deleteById(int id) {
        database.remove(id);
    }

    @Override
    public List<RoomEntity> findAll() {
        return new ArrayList<>(database.values());
    }
}
