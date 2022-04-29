package chess.dao;

import chess.entity.Room;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoomFakeDao implements RoomDao {

    private final Map<Long, Room> memoryDbRoom = new HashMap<>();
    private Long autoIncrementId = 0L;

    @Override
    public List<Room> findAllRoom() {
        return memoryDbRoom.entrySet().stream()
                .map(entry -> new Room(
                        entry.getValue().getId(), entry.getValue().getState(),
                        entry.getValue().getTitle(), entry.getValue().getPassword()))
                .collect(Collectors.toList());
    }

    @Override
    public Long insertRoom(String title, String password) {
        memoryDbRoom.put(autoIncrementId, new Room(autoIncrementId, "Ready", title, password));

        return autoIncrementId++;
    }

    @Override
    public Long updateStateById(Long roomId, String state) {
        final Room room = memoryDbRoom.get(roomId);
        memoryDbRoom.replace(roomId, new Room(room.getId(), state, room.getTitle(), room.getPassword()));
        return roomId;
    }

    @Override
    public Room findRoomById(Long roomId) {
        return memoryDbRoom.get(roomId);
    }

    @Override
    public Long deleteRoom(Long roomId) {
        return memoryDbRoom.remove(roomId).getId();
    }
}
