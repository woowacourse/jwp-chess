package chess.domain.repository.room;

import chess.domain.game.Room;
import java.util.List;

public interface RoomRepository {

    long insert(String name);

    long insert(Room room);

    Room findByName(String name);

    List<Room> findAll();

    void save(Room room);

    void saveAfterMove(Room room, String source, String target);

    boolean exists(String name);

    void deleteAll();
}
