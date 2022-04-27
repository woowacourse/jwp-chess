package chess.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import chess.ChessGameVO;
import chess.db.ChessGameDao;
import chess.db.PieceDao;
import chess.domain.ChessGame;
import chess.domain.GameResult;
import chess.domain.GameTurn;
import chess.domain.board.Board;
import chess.domain.board.InitialBoardGenerator;
import chess.domain.board.SavedBoardGenerator;
import chess.domain.position.Square;

@Service
public class ChessService {
    private final ChessGameDao chessGameDao;
    private final PieceDao pieceDao;

    public ChessService(ChessGameDao chessGameDao, PieceDao pieceDao) {
        this.chessGameDao = chessGameDao;
        this.pieceDao = pieceDao;
    }

    public ChessGame loadChessGame(String gameID, String restart) {
        try {
            pieceDao.findByGameID(gameID);
        } catch (IllegalArgumentException e) {
            initBoard(gameID);
        }
        if ("true".equals(restart)) {
            initBoard(gameID);
        }
        loadTurn(gameID, restart);
        return loadGame(gameID);
    }

    private void initBoard(String gameID) {
        pieceDao.deleteAll(gameID);
        pieceDao.initPieces(gameID);
    }

    private void loadTurn(String gameID, String restart) {
        if ("true".equals(restart)) {
            chessGameDao.initTurn(gameID);
        }
    }

    private ChessGame loadGame(String gameID) {
        ChessGame chessGame;
        try {
            GameTurn gameTurn = getTurn(gameID);
            checkCanContinue(gameTurn);
            chessGame = loadSavedChessGame(gameID, gameTurn);
        } catch (RuntimeException e) {
            chessGame = loadNewChessGame();
            startGame(gameID, chessGame);
        }
        return chessGame;
    }

    public GameTurn getTurn(String gameID) {
        return GameTurn.find(chessGameDao.findTurnByID(gameID));
    }

    private void checkCanContinue(GameTurn gameTurn) {
        if (GameTurn.FINISHED.equals(gameTurn)) {
            throw new IllegalArgumentException();
        }
    }

    public ChessGame loadSavedChessGame(String gameID, GameTurn gameTurn) {
        return new ChessGame(new SavedBoardGenerator(pieceDao.findByGameID(gameID)), gameTurn);
    }

    private ChessGame loadNewChessGame() {
        return new ChessGame(new InitialBoardGenerator(), GameTurn.READY);
    }

    private void startGame(String gameID, ChessGame chessGame) {
        chessGameDao.save(gameID, chessGame);
        updateTurn(gameID, chessGame);
    }

    public void movePiece(String gameID, ChessGame chessGame, String source, String target) {
        updateTurn(gameID, chessGame);
        updatePosition(gameID, source, target);
    }

    private void updateTurn(String gameID, ChessGame chessGame) {
        chessGameDao.updateTurn(gameID, chessGame);
    }

    private void updatePosition(String gameID, String source, String target) {
        pieceDao.deleteByPosition(new Square(target), gameID);
        pieceDao.updatePosition(new Square(source), new Square(target), gameID);
        pieceDao.insertNone(gameID, new Square(source));
    }

    public GameResult getGameResult(String gameID) {
        Board board = new Board(new SavedBoardGenerator(pieceDao.findByGameID(gameID)));
        return new GameResult(board);
    }

    public List<ChessGameVO> getGameIDs() {
        return chessGameDao.findAllGame().stream()
                .map(ChessGameVO::new)
                .collect(Collectors.toList());
    }
}
