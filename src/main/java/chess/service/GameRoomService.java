package chess.service;

import chess.dao.GameDao;
import chess.dto.GameRoomDataDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GameRoomService {

    private final GameDao gameDao;

    public GameRoomService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public List<GameRoomDataDto> getAllGameRooms() {
        return gameDao.findAllIdAndTitle();
    }
}
