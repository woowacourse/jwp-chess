package chess.service;

import chess.dao.RoomDao;
import chess.entity.RoomEntity;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomDao roomDao;

    public RoomService(final RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public List<RoomEntity> searchRooms() {
        return roomDao.findAll();
    }

    public List<RoomEntity> createRoom(final String name, final String password) {
        roomDao.insert(name, password);
        return roomDao.findAll();
    }
}
