package chess.repository;

import chess.model.room.Room;
import chess.repository.dao.GameDao;
import chess.repository.dao.entity.GameEntity;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepository {
    private final GameDao gameDao;

    public RoomRepository(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public Room findById(Integer gameId) {
        GameEntity entity = gameDao.findById(gameId);
        return Room.fromHashedPassword(entity.getName(), entity.getPassword());
    }

    public int createRoom(Room room) {
        return gameDao.createGame(room.getName(), room.getPassword());
    }

    public int deleteRoom(Integer gameId) {
        return gameDao.deleteGame(gameId);
    }
}
