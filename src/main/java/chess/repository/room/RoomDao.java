package chess.repository.room;

import chess.domain.game.Room;

public interface RoomDao {

    long insert(Room room);

    void update(Room room);

    Room findRoomByName(String name);

    boolean isExistName(String name);

    Room findRoomById(long roomId);

    void deleteAll();
}
