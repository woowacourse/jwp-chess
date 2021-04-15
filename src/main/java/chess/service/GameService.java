package chess.service;

import chess.dao.GameDao;
import chess.dto.GameRequestDto;
import chess.dto.GameResponseDto;
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

    public GameResponseDto findById(long gameId) {
        return GameResponseDto.from(gameDao.findById(gameId));
    }
}
