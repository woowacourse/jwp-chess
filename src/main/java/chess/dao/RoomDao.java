package chess.dao;

import chess.entity.Room;
import java.util.Optional;

public interface RoomDao {
    void save(Room room);

    Optional<Room> findByName(String name);

    void update(long id, String turn);
}
