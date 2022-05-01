package chess.service;

import chess.dao.RoomDao;
import chess.dto.request.RoomRequest;
import chess.dto.response.RoomResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomDao roomDao;

    public RoomService(final RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public List<RoomResponse> searchRooms() {
        return roomDao.findAll();
    }

    public List<RoomResponse> createRoom(final RoomRequest roomRequest) {
        roomDao.insert(roomRequest.getName(), roomRequest.getPassword());
        return roomDao.findAll();
    }

    public List<RoomResponse> deleteRoomFrom(String id) {
        roomDao.deleteFrom(id);
        return roomDao.findAll();
    }
}
