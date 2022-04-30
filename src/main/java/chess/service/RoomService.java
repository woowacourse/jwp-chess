package chess.service;

import chess.entity.RoomEntity;
import chess.repository.RoomDao;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomDao roomDao;

    public RoomService(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public RoomEntity create(String name, String password) {
        return roomDao.insert(new RoomEntity(name, password));
    }

    public List<RoomEntity> findAllRooms() {
        return roomDao.findAll();
    }

    public boolean delete(Long id, String password) {
        if (roomDao.findById(id).getPassword().equals(password)) {
            roomDao.deleteById(id);
            return true;
        }
        return false;
    }
}
