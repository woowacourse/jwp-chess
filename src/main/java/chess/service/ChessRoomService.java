package chess.service;

import chess.webdao.RoomDao;
import chess.webdto.dao.RoomDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ChessRoomService {
    private final RoomDao roomDao;

    public ChessRoomService(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public List<RoomDto> showRooms() {
        return roomDao.selectAllRooms();
    }

    @Transactional
    public long createNewRoom(String roomName) {
        return roomDao.createRoom("white", true, roomName);
    }

    @Transactional
    public long createNewRoom(String roomName, String password) {
        return roomDao.createRoom("white", true, roomName, password);
    }
}
