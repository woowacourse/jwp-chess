package chess.service;

import chess.dao.GameDao;
import chess.dto.GameRequestDto;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private GameDao gameDao;

    public GameService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public long add(GameRequestDto gameRequestDto) {
        return gameDao.insert(gameRequestDto.toEntity());
    }
}
