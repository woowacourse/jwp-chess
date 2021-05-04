package chess.dao.room;

import chess.domain.game.Room;
import java.util.List;

public interface RoomDao {

    long insert(Room room);

    void update(Room room);

    Room findByName(String name);

    boolean roomExists(String name);

    Room findById(long roomId);

    List<Room> findAll();

    void deleteAll();
}
