package chess.repository.room;

import chess.domain.game.Room;
import java.util.Optional;

public interface RoomRepository {

    long insert(String name);

    long insert(Room room);

    Room findRoomByName(String name);

    void save(Room room);

    void saveAfterMove(Room room, String source, String target);

    boolean exists(String name);

    void deleteAll();
}
