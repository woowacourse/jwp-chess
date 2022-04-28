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

    public ChessGame loadChessGame(ChessGameVO chessGameVO, String restart) {
        String gameID = chessGameVO.getGameID();
        try {
            pieceDao.findByGameID(gameID);
        } catch (IllegalArgumentException e) {
            initBoard(gameID);
        }
        if ("true".equals(restart)) {
            initBoard(gameID);
        }
        loadTurn(gameID, restart);
        return loadGame(chessGameVO);
    }

    private void initBoard(String gameID) {
        pieceDao.deleteByGameID(gameID);
        pieceDao.initPieces(gameID);
    }

    private void loadTurn(String gameID, String restart) {
        if ("true".equals(restart)) {
            chessGameDao.initTurn(gameID);
        }
    }

    private ChessGame loadGame(ChessGameVO chessGameVO) {
        String gameID = chessGameVO.getGameID();
        ChessGame chessGame;
        try {
            GameTurn gameTurn = getTurn(gameID);
            checkCanContinue(gameTurn);
            chessGame = loadSavedChessGame(chessGameVO);
        } catch (RuntimeException e) {
            chessGame = loadNewChessGame();
            startGame(gameID, chessGameVO.getPassword(), chessGame);
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

    public ChessGame loadSavedChessGame(ChessGameVO chessGameVO) {
        return new ChessGame(new SavedBoardGenerator(pieceDao.findByGameID(chessGameVO.getGameID())), getTurn(chessGameVO.getGameID()));
    }

    private ChessGame loadNewChessGame() {
        return new ChessGame(new InitialBoardGenerator(), GameTurn.READY);
    }

    private void startGame(String gameID, String password, ChessGame chessGame) {
        chessGameDao.save(gameID, password, chessGame);
        updateTurn(gameID, chessGame);
    }

    public void movePiece(ChessGameVO chessGameVO, ChessGame chessGame, String source, String target) {
        String gameID = chessGameVO.getGameID();
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

    public GameResult getGameResult(ChessGameVO chessGameVO) {
        Board board = new Board(new SavedBoardGenerator(pieceDao.findByGameID(chessGameVO.getGameID())));
        return new GameResult(board);
    }

    public List<ChessGameVO> getGameIDs() {
        return chessGameDao.findAllGame().stream()
                .map(ChessGameVO::new)
                .collect(Collectors.toList());
    }

    public void deleteGameByGameID(ChessGameVO chessGameVO) {
        String gameID = chessGameVO.getGameID();
        String password = chessGameVO.getPassword();
        checkPassword(gameID, password);
        checkCanDelete(GameTurn.find(chessGameDao.findTurnByID(gameID)));
        chessGameDao.deleteByGameID(gameID, password);
        pieceDao.deleteByGameID(gameID);
    }

    private void checkPassword(String gameID, String password) {
        if (!chessGameDao.findPasswordByGameID(gameID, password)) {
            throw new IllegalArgumentException("비밀 번호 틀렸지롱~ 🤪");
        }
    }

    private void checkCanDelete(GameTurn gameTurn) {
        if (!GameTurn.FINISHED.equals(gameTurn)) {
            throw new IllegalArgumentException("아직 게임이 진행 중이라구!! 😡");
        }
    }
}
