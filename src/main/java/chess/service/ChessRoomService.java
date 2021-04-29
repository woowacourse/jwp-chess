package chess.service;

import chess.webdao.RoomDao;
import chess.webdto.dao.RoomDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ChessRoomService {
    private final RoomDao roomDao;

    public ChessRoomService(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public List<RoomDto> showRooms(){
        return roomDao.selectAllRooms();
    }

    public void createNewRoom(){

    }
}
