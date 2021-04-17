package chess.service;

import chess.dao.GameDao;
import chess.dto.GameRequestDto;
import chess.dto.GameResponseDto;
import chess.dto.RoomResponseDto;
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

    public void endGame(long gameId, boolean isFinished) {
        gameDao.updateGameStatus(gameId, isFinished);
    }

    public void changeTurn(long gameId) {
        gameDao.updateTurn(gameId);
    }

    public String getTurn(long gameId) {
        return gameDao.getTurn(gameId);
    }

    public RoomResponseDto getRoomNumber() {
        return RoomResponseDto.from(gameDao.getRoomNumbers());
    }

}
