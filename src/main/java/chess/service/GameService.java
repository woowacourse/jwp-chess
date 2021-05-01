package chess.service;

import chess.dao.GameDao;
import chess.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<RoomResponseDto> getRooms() {
        return gameDao.getRooms();
    }

    public int gameCount() {
        return gameDao.gameCount();
    }

    public int deleteByGameId(long roomId) {
        return gameDao.deleteByGameId(roomId);
    }

    public RoomExistResponseDto findGameByName(String roomName) {
        return gameDao.findByName(roomName);
    }
}
