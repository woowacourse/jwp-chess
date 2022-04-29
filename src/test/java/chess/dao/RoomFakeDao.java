package chess.dao;

import chess.entity.Room;
import java.util.HashMap;
import java.util.Map;

public class RoomFakeDao implements RoomDao {

    private final Map<Long, Room> memoryDbRoom = new HashMap<>();
    private Long autoIncrementId = 0L;

    @Override
    public Long insertRoom(String title, String password) {
        memoryDbRoom.put(autoIncrementId, new Room(autoIncrementId, "Ready", title, password));

        return autoIncrementId++;
    }
}
