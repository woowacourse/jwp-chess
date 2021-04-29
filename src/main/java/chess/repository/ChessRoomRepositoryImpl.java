package chess.repository;

import chess.dao.GameDao;
import chess.dao.RoomDao;
import chess.dao.UserDao;
import chess.domain.room.ChessRoomRepository;
import chess.domain.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChessRoomRepositoryImpl implements ChessRoomRepository {

    private final GameDao gameDao;
    private final RoomDao roomDao;
    private final UserDao userDao;

    @Autowired
    public ChessRoomRepositoryImpl(final GameDao gameDao, final RoomDao roomDao, final UserDao userDao) {
        this.gameDao = gameDao;
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

    @Override
    public void deleteUserFormRoom(final Long roomId, final String userName) {
        roomDao.deleteUserFromRoom(roomId, userName);
    }

    @Override
    public void deleteRoom(final Room room) {
        roomDao.deleteRoom(room.getId());
        gameDao.delete(room.getGameId());
    }
}
