package chess.service;

import chess.entity.RoomEntity;
import chess.exception.DeleteRoomException;
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

    public RoomEntity findById(Long roomId) {
        return roomDao.findById(roomId);
    }

    public void delete(Long id, String password) {
        if (!roomDao.findById(id).getPassword().equals(password)) {
            throw new DeleteRoomException("비밀번호를 잘못 입력했습니다.");
        }
        roomDao.deleteById(id);
    }
}
