package chess.service;

import chess.dao.RoomDao;
import chess.dto.RoomDto;
import chess.entity.RoomEntity;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomDao roomDao;

    public RoomService(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public List<RoomEntity> search() {
        return roomDao.findAll();
    }

    public RoomDto create(RoomDto roomDto) {
        roomDao.insert(roomDto.getName(), roomDto.getPassword());
        return roomDto;
    }
}
