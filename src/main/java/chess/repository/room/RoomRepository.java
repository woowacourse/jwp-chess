package chess.repository.room;

import chess.domain.game.Room;
import java.sql.SQLException;

public interface RoomRepository {

    long insert(Room room);

    void update(Room room);

    Room findRoomByName(String name);

    boolean isExistName(String name);

    Room findRoomById(long roomId);

    void deleteAll();
}
