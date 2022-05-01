package chess.repository;

import chess.domain.Room;
import chess.dto.GameCreateRequest;
import chess.repository.dao.RoomDao;
import chess.repository.entity.RoomEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class RoomJdbcRepository implements RoomRepository {

    private final RoomDao roomDao;

    public RoomJdbcRepository(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Override
    public long save(GameCreateRequest gameCreateRequest) {
        return roomDao.save(RoomEntity.from(gameCreateRequest));
    }

    @Override
    public Room findById(long id) {
        return Room.from(roomDao.findById(id));
    }

    @Override
    public List<Room> findAll() {
        return roomDao.findAll()
                .stream()
                .map(Room::from)
                .collect(Collectors.toList());
    }

    @Override
    public long deleteById(long id) {
        return roomDao.deleteById(id);
    }

    @Override
    public long deleteAll() {
        return roomDao.deleteAll();
    }

    @Override
    public void changeTurnById(long id) {
        roomDao.changeTurnById(id);
    }

    @Override
    public void finishById(long id) {
        roomDao.finishById(id);
    }
}
