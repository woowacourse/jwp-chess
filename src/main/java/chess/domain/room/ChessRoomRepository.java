package chess.domain.room;

import java.util.List;

public interface ChessRoomRepository {
    Long create(final Room room, final Long gameId);
    Room room(Long roomId);
    List<Room> rooms();
}
