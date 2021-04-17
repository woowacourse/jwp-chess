package chess.service;

import chess.dao.GameDAO;
import chess.domain.ChessGameManager;
import chess.domain.position.Position;
import chess.dto.GameListDto;
import chess.dto.NewGameResponse;
import chess.dto.RunningGameResponse;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {
    private GameDAO gameDAO;

    public ChessGameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public NewGameResponse createNewGame() {
        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.start();
        int gameId = gameDAO.saveNewGame(chessGameManager);
        return NewGameResponse.from(chessGameManager, gameId);

    }

    public RunningGameResponse move(int gameId, Position from, Position to) {
        ChessGameManager chessGameManager = gameDAO.loadGame(gameId);
        chessGameManager.move(from, to);

        gameDAO.updateTurnByGameId(chessGameManager, gameId);
        gameDAO.updatePiecesByGameId(chessGameManager, gameId);

        return RunningGameResponse.from(chessGameManager);
    }

    public GameListDto loadAllGames() {
        return GameListDto.from(gameDAO.loadGames());
    }

    public ChessGameManager loadChessGameById(int id) {
        return gameDAO.loadGame(id);
    }
}
