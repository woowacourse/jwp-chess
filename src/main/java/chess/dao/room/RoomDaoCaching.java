package chess.dao.room;

import chess.domain.ChessGameImpl;
import chess.domain.room.Room;
import chess.domain.room.RoomInformation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDaoCaching implements RoomDao {
    private static Long roomId = 1L;

    private final List<Room> rooms = new ArrayList<>();

    @Override
    public Room newRoom(RoomInformation roomInformation) {
        final Room room = new Room(roomId++, roomInformation, ChessGameImpl.initialGame());
        rooms.add(room);
        return room;
    }

    @Override
    public List<Room> rooms() {
        return Collections.unmodifiableList(rooms);
    }

    @Override
    public Optional<Room> findRoom(Long roomId) {
        return rooms.stream()
            .filter(room -> room.hasSameId(roomId))
            .findAny();
    }
}
