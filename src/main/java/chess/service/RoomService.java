package chess.service;

import chess.dao.RoomDao;
import chess.dto.RoomDto;
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

    public List<RoomEntity> createRoom(final RoomDto roomDto) {
        roomDao.insert(roomDto.getName(), roomDto.getPassword());
        return roomDao.findAll();
    }
}
