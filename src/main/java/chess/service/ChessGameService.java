package chess.service;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.ChessGameManager;
import chess.domain.board.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.dto.GameListDto;
import chess.dto.NewGameResponse;
import chess.dto.RunningGameResponse;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {
    private GameDao gameDao;
    private PieceDao pieceDao;

    public ChessGameService(GameDao gameDao, PieceDao pieceDao) {
        this.gameDao = gameDao;
        this.pieceDao = pieceDao;
    }

    public NewGameResponse createNewGame() {
        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.start();
        int gameId = gameDao.saveNewGame(chessGameManager);
        pieceDao.savePiecesByGameId(chessGameManager, gameId);
        return NewGameResponse.from(chessGameManager, gameId);
    }

    public RunningGameResponse move(int gameId, Position from, Position to) {
        ChessGameManager chessGameManager = loadChessGameByGameId(gameId);

        chessGameManager.move(from, to);

        gameDao.updateTurnByGameId(chessGameManager, gameId);
        pieceDao.updatePiecesByGameId(chessGameManager, gameId);

        return RunningGameResponse.from(chessGameManager);
    }

    public ChessGameManager loadChessGameByGameId(int gameId) {
        ChessBoard chessBoard = pieceDao.loadChessBoardByGameId(gameId);
        Color currentTurn = gameDao.loadCurrentTurnByGameId(gameId);

        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.load(chessBoard, currentTurn);
        return chessGameManager;
    }

    public GameListDto loadAllGames() {
        return GameListDto.from(gameDao.loadGames());
    }
}
