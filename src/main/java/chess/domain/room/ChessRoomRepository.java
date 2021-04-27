package chess.domain.room;

import java.util.List;

public interface ChessRoomRepository {
    Long create(final Room room);
    Room room(final Long roomId);
    List<Room> rooms();
}
