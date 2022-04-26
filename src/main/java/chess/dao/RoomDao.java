package chess.dao;

import chess.entity.Room;
import java.util.Optional;

public interface RoomDao {
    long save(Room room);

    Optional<Room> findByName(String name);

    Optional<Room> findById(long id);

    void updateTurn(long id, String turn);
}
