package chess.service;

import chess.repository.RoomRepository;
import chess.web.dto.RoomDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
}
