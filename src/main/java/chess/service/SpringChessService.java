package chess.service;

import chess.dao.spring.PlayLogDao;
import chess.dao.spring.RoomDao;
import chess.dao.spring.UserDao;
import chess.dto.web.BoardDto;
import chess.dto.web.RoomDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SpringChessService {

    private RoomDao roomDao;
    private UserDao userDao;
    private PlayLogDao playLogDao;

    public SpringChessService(RoomDao roomDao, UserDao userDao, PlayLogDao playLogDao) {
        this.roomDao = roomDao;
        this.userDao = userDao;
        this.playLogDao = playLogDao;
    }

    public List<RoomDto> openedRooms() {
        return roomDao.openedRooms();
    }

    public BoardDto latestBoard(String id) {
        return playLogDao.latestBoard(id);
    }

    public Object usersInRoom(String id) {
        return userDao.usersInRoom(id);
    }
}
