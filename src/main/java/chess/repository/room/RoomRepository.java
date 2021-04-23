package chess.repository.room;

import chess.domain.game.Room;

public interface RoomRepository {

    long insert(Room room);

    void update(Room room);

    Room findByName(String name);

    boolean isExistRoomName(String name);

    Room findById(long roomId);

    void deleteAll();
}
