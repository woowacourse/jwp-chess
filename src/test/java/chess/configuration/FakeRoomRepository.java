package chess.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import chess.domain.Room;
import chess.repository.RoomRepository;

public class FakeRoomRepository implements RoomRepository {

    private int autoIncrementId = 0;
    private final Map<Integer, Room> database = new HashMap<>();

    @Override
    public int save(Room room) {
        autoIncrementId++;
        database.put(autoIncrementId,
            new Room(autoIncrementId, room));
        return autoIncrementId;
    }

    @Override
    public Optional<Room> findByName(String name) {
        return database.keySet().stream()
            .filter(key -> database.get(key).getName().equals(name))
            .map(key -> new Room(
                key, new Room(database.get(key).getName(), database.get(key).getPassword(), false)))
            .findAny();
    }

    @Override
    public Optional<Room> findById(int roomId) {
        return Optional.ofNullable(database.get(roomId));
    }

    @Override
    public void deleteById(int id) {
        database.remove(id);
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public void updateEndByBoardId(int boardId, boolean isEnd) {
        database.values().forEach(Room::finish);
    }
}
