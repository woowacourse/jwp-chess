package chess.dao.room;

import chess.domain.game.Room;
import java.util.List;

public interface RoomDao {

    long insert(Room room);

    void update(Room room);

    Room findRoomByName(String name);

    Room findRoomById(long roomId);

    List<Room> findAll();

    boolean isExistName(String name);

    void deleteAll();
}
