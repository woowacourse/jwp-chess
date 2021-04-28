package chess.service;

import chess.dao.GameDao;
import chess.dao.dto.GameDto;
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
        return gameDao.insert(gameRequestDto.toGameDto());
    }

    public GameResponseDto findById(final long gameId) {
        final GameDto gameDto = gameDao.findById(gameId);
        return GameResponseDto.from(gameDto);
    }

    public void endGame(final long gameId) {
        gameDao.updateGameStatus(gameId, true);
    }

    public void changeTurn(final long gameId) {
        gameDao.updateTurn(gameId);
    }

}
