package chess.repository;

import chess.dao.RoomDao;
import chess.dao.UserDao;
import chess.domain.room.ChessRoomRepository;
import chess.domain.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChessRoomRepositoryImpl implements ChessRoomRepository {

    private final RoomDao roomDao;
    private final UserDao userDao;

    @Autowired
    public ChessRoomRepositoryImpl(final RoomDao roomDao, final UserDao userDao) {
        this.roomDao = roomDao;
        this.userDao = userDao;
    }

    @Override
    public Long create(final Room room) {
        Long roomId = roomDao.create(room);
        String userName = room.getWhitePlayer();
        userDao.setRoomId(roomId, userName);
        return roomId;
    }

    @Override
    public Room room(final Long roomId) {
        return roomDao.load(roomId);
    }

    public void join(String blackPlayer, Long roomId) {
        userDao.setRoomId(roomId, blackPlayer);
        roomDao.setBlackPlayer(blackPlayer, roomId);
    }

    @Override
    public List<Room> rooms() {
        return roomDao.loadAll();
    }
}
