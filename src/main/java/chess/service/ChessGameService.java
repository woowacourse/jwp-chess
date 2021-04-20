package chess.service;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.ChessGameManager;
import chess.domain.board.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.GameListDto;
import chess.dto.NewGameDto;
import chess.dto.RunningGameDto;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {
    private final GameDao gameDao;
    private final PieceDao pieceDao;

    public ChessGameService(GameDao gameDao, PieceDao pieceDao) {
        this.gameDao = gameDao;
        this.pieceDao = pieceDao;
    }

    public NewGameDto createNewGame() {
        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.start();
        int gameId = gameDao.saveNewGame(chessGameManager);
        pieceDao.savePiecesByGameId(chessGameManager, gameId);
        return NewGameDto.from(chessGameManager, gameId);
    }

    public RunningGameDto move(int gameId, Position from, Position to) {
        ChessGameManager chessGameManager = loadChessGameByGameId(gameId);

        chessGameManager.move(from, to);

        Piece pieceToMove = pieceDao.loadPieceByPosition(from, gameId);
        pieceDao.deletePieceByPosition(to, gameId);
        pieceDao.savePiece(pieceToMove, to, gameId);
        pieceDao.deletePieceByPosition(from, gameId);

        gameDao.updateTurnByGameId(chessGameManager, gameId);

        return RunningGameDto.from(chessGameManager);
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
