package chess.configuration;

import chess.repository.RoomRepository;
import chess.web.dto.RoomDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeRoomRepository implements RoomRepository {

    private final Map<Integer, RoomDto> database = new HashMap<>();
    private int autoIncrementId = 0;

    @Override
    public int save(RoomDto roomDto) {
        autoIncrementId++;
        database.put(autoIncrementId,
            new RoomDto(autoIncrementId, roomDto.getName(), roomDto.getPassword()));
        return autoIncrementId;
    }

    @Override
    public Optional<RoomDto> findByName(String name) {
        return database.keySet().stream()
            .filter(key -> database.get(key).getName().equals(name))
            .map(key -> new RoomDto(key, database.get(key).getName(), database.get(key).getPassword()))
            .findAny();
    }

    @Override
    public Optional<RoomDto> findById(int roomId) {
        return Optional.ofNullable(database.get(roomId))
            .map(room -> new RoomDto(roomId, room.getName(), room.getPassword()));
    }

    @Override
    public void deleteById(int id) {
        database.remove(id);
    }

    @Override
    public List<RoomDto> findAll() {
        return new ArrayList<>(database.values());
    }
}
