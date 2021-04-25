package chess.repository;

import chess.dao.RoomDao;
import chess.domain.room.ChessRoomRepository;
import chess.domain.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChessRoomRepositoryImpl implements ChessRoomRepository {

    private final RoomDao roomDao;

    @Autowired
    public ChessRoomRepositoryImpl(final RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Override
    public Long create(final Room room, Long gameId) {
        room.setGameId(gameId);
        return roomDao.create(room);
    }

    @Override
    public Room room(final Long roomId) {
        return roomDao.load(roomId);
    }

    @Override
    public List<Room> rooms() {
        return roomDao.loadAll();
    }
}
