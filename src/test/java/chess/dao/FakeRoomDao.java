package chess.dao;

import chess.web.dto.RoomDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeRoomDao {

    private final Map<Integer, Room> database = new HashMap<>();
    private int autoIncrementId = 0;

    public int save(String name, String password) {
        autoIncrementId++;
        database.put(autoIncrementId, new Room(name, password));
        return autoIncrementId;
    }

    public List<RoomDto> findAll() {
        return database.keySet().stream()
                .map(key -> new RoomDto(key, database.get(key).name, database.get(key).password))
                .collect(Collectors.toList());
    }

    public Optional<RoomDto> find(String name) {
        return database.keySet().stream()
                .filter(key -> database.get(key).equals(name))
                .map(key -> new RoomDto(key, database.get(key).name, database.get(key).password))
                .findAny();
    }

    public Optional<RoomDto> findById(int roomId) {
        return Optional.ofNullable(database.get(roomId))
                .map(room -> new RoomDto(roomId, room.name, room.password));
    }

    public void delete(int roomId) {
        database.remove(roomId);
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
