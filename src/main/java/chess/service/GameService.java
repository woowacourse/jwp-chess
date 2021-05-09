package chess.service;

import chess.controller.dto.GameDto;
import chess.dao.GameDao;
import chess.exception.ChessException;
import chess.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameDao gameDao;

    public GameService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public Long create(GameDto gameDto) {
        return gameDao.insert(gameDto.getGameName());
    }

    public void delete(Long roomId) {
        int deleteCount = gameDao.delete(roomId);
        if (deleteCount == 0) {
            throw new ChessException(ErrorCode.NO_ROOM_TO_DELETE);
        }
    }

    public List<GameDto> allGame() {
        return gameDao.selectAll();
    }
}
