package chess.dao.room;

import chess.domain.room.Room;
import chess.domain.room.RoomInformation;
import java.util.List;
import java.util.Optional;

public interface RoomDao {

    Room newRoom(RoomInformation roomInformation);

    List<Room> rooms();

    Optional<Room> findRoom(Long roomId);

    void removeRoom(Long id);
}
