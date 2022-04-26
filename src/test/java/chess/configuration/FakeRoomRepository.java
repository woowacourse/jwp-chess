package chess.configuration;

import chess.repository.RoomRepository;
import chess.web.dto.RoomDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeRoomRepository implements RoomRepository {

    private final Map<Integer, String> database = new HashMap<>();
    private int autoIncrementId = 0;

    @Override
    public int save(String name) {
        autoIncrementId++;
        database.put(autoIncrementId, name);
        return autoIncrementId;
    }

    @Override
    public Optional<RoomDto> find(String name) {
        return database.keySet().stream()
            .filter(key -> database.get(key).equals(name))
            .map(key -> new RoomDto(key, database.get(key)))
            .findAny();
    }

    @Override
    public Optional<RoomDto> findById(int roomId) {
        return Optional.ofNullable(database.get(roomId))
            .map(name -> new RoomDto(roomId, name));
    }

    @Override
    public void deleteById(int id) {
        database.remove(id);
    }

    @Override
    public List<RoomDto> findAll() {
        return database.entrySet().stream()
            .map(entry -> new RoomDto(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }
}
