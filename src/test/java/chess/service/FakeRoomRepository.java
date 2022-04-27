package chess.service;

import chess.repository.RoomRepository;
import chess.web.dto.RoomDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeRoomRepository implements RoomRepository {

    private final Map<Integer, Room> database = new HashMap<>();
    private int autoIncrementId = 0;

    @Override
    public int save(String name, String password) {
        autoIncrementId++;
        database.put(autoIncrementId, new Room(name, password));
        return autoIncrementId;
    }

    @Override
    public Optional<RoomDto> find(String name) {
        return database.keySet().stream()
                .filter(key -> database.get(key).equals(name))
                .map(key -> new RoomDto(key, database.get(key).name, database.get(key).password))
                .findAny();
    }

    @Override
    public Optional<RoomDto> findById(int roomId) {
        return Optional.ofNullable(database.get(roomId))
                .map(room -> new RoomDto(roomId, room.name, room.password));
    }

    private static class Room {
        private String name;
        private String password;

        public Room(String name, String password) {
            this.name = name;
            this.password = password;
        }
    }
}
