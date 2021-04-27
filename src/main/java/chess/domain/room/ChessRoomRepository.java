package chess.domain.room;

import java.util.List;

public interface ChessRoomRepository {
    Long create(final Room room);
    Room room(final Long roomId);
    void join(String blackPlayer, Long roomId);
    List<Room> rooms();
}
