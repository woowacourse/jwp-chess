package chess.service;

import chess.entity.RoomEntity;
import chess.repository.RoomDao;
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
}
