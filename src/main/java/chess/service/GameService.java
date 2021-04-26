package chess.service;

import chess.dao.GameDao;
import chess.dto.game.GameRequestDto;
import chess.dto.game.GameResponseDto;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameDao gameDao;

    public GameService(final GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public long add(final GameRequestDto gameRequestDto) {
        return gameDao.insert(gameRequestDto.toEntity());
    }

    public GameResponseDto findById(final long gameId) {
        return GameResponseDto.from(gameDao.findById(gameId));
    }

    public void endGame(final long gameId) {
        gameDao.updateGameStatus(gameId, true);
    }

    public void changeTurn(final long gameId) {
        gameDao.updateTurn(gameId);
    }
}
