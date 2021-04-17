package chess.service;

import chess.dao.GameDAO;
import chess.domain.ChessGameManager;
import chess.dto.NewGameResponse;
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
}
