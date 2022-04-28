package chess.service;

import chess.db.ChessGameDao;
import chess.db.PieceDao;

import chess.domain.game.ChessGame;
import chess.domain.game.GameEntity;
import chess.domain.game.GameResult;
import chess.domain.game.GameTurn;
import chess.domain.board.Board;
import chess.domain.board.InitialBoardGenerator;
import chess.domain.board.SavedBoardGenerator;
import chess.domain.position.Square;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChessService {
    private final ChessGameDao chessGameDao;
    private final PieceDao pieceDao;

    public ChessService(final ChessGameDao chessGameDao, final PieceDao pieceDao) {
        this.chessGameDao = chessGameDao;
        this.pieceDao = pieceDao;
    }

    public List<GameEntity> loadGameLists() {
        return chessGameDao.findAllGames();
    }

    public ChessGame loadGame(String gameID) {
        GameTurn gameTurn = getTurn(gameID);
        return loadSavedChessGame(gameID, gameTurn);
    }

    public void createGame(String gameID, String gamePW) {
        ChessGame chessGame = loadNewChessGame();
        saveNewGame(gameID, gamePW, chessGame);
        loadPieces(gameID);
    }

    public void deleteGame(String gameID) {
        chessGameDao.delete(gameID);
        pieceDao.deleteAll(gameID);
    }

    public boolean checkPassword(String gameID, String inputPW) {
        return chessGameDao.checkPassword(gameID, inputPW);
    }

    public GameTurn getTurn(String gameID) {
        return GameTurn.find(chessGameDao.findTurnByID(gameID));
    }

    public boolean isFinished(String gameID) {
        GameTurn gameTurn = getTurn(gameID);
        return GameTurn.FINISHED.equals(gameTurn);
    }

    public ChessGame loadSavedChessGame(String gameID, GameTurn gameTurn) {
        return new ChessGame(new SavedBoardGenerator(pieceDao.findByGameID(gameID)), gameTurn);
    }

    private ChessGame loadNewChessGame() {
        return new ChessGame(new InitialBoardGenerator(), GameTurn.READY);
    }

    public void saveNewGame(String gameID, String gamePW, ChessGame chessGame) {
        chessGameDao.save(gameID, gamePW, chessGame);
        updateTurn(gameID, chessGame);
    }

    public void updateTurn(String gameID, ChessGame chessGame) {
        chessGameDao.updateTurn(gameID, chessGame);
    }

    public void loadPieces(String gameID) {
        pieceDao.deleteAll(gameID);
        pieceDao.save(gameID);
    }

    public GameResult getGameResult(String gameID) {
        Board board = new Board(new SavedBoardGenerator(pieceDao.findByGameID(gameID)));
        return new GameResult(board);
    }

    public void movePiece(String gameID, String source, String target) {
        pieceDao.deleteByPosition(new Square(target), gameID);
        pieceDao.updatePosition(new Square(source), new Square(target), gameID);
        pieceDao.insertNone(gameID, new Square(source));
    }

    public String findGameID(String gameCode) {
        return chessGameDao.findIDByCode(gameCode);
    }

    public void restartGame(String gameID) {
        ChessGame chessGame = loadNewChessGame();
        updateTurn(gameID, chessGame);
    }

    public void startGame(String gameID, ChessGame chessGame) {
        chessGame.startGame();
        updateTurn(gameID, chessGame);
    }
}
