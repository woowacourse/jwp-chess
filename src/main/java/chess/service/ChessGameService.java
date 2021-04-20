package chess.service;

import chess.dao.GameDao;
import chess.domain.ChessGameManager;
import chess.domain.position.Position;
import chess.dto.GameListDto;
import chess.dto.NewGameResponse;
import chess.dto.RunningGameResponse;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {
    private GameDao gameDao;

    public ChessGameService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public NewGameResponse createNewGame() {
        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.start();
        int gameId = gameDao.saveNewGame(chessGameManager);
        return NewGameResponse.from(chessGameManager, gameId);

    }

    public RunningGameResponse move(int gameId, Position from, Position to) {
        ChessGameManager chessGameManager = gameDao.loadGame(gameId);
        chessGameManager.move(from, to);

        gameDao.updateTurnByGameId(chessGameManager, gameId);
        gameDao.updatePiecesByGameId(chessGameManager, gameId);

        return RunningGameResponse.from(chessGameManager);
    }

    public GameListDto loadAllGames() {
        return GameListDto.from(gameDao.loadGames());
    }

    public ChessGameManager loadChessGameById(int id) {
        return gameDao.loadGame(id);
    }
}
