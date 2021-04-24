package chess.service;

import chess.controller.dto.GameDto;
import chess.dao.GameDao;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GameService {
    private final GameDao gameDao;

    public GameService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public Long create(String roomName) {
        return gameDao.insert(roomName);
    }

    public void delete(Long roomId) {
        gameDao.delete(roomId);
    }

    public List<GameDto> load() {
        return gameDao.selectAll();
    }
}
