package chess.service;

import chess.controller.dto.RoomDto;
import chess.dao.RoomDao;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoomService {
    private final RoomDao roomDao;

    public RoomService(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public Long create(String roomName) {
        return roomDao.insert(roomName);
    }

    public void delete(Long roomId) {
        roomDao.delete(roomId);
    }

    public List<RoomDto> load() {
        return roomDao.selectAll();
    }
}
