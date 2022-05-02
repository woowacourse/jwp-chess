package chess.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import chess.db.ChessGameDao;
import chess.db.PieceDao;
import chess.domain.ChessGame;
import chess.domain.GameResult;
import chess.domain.GameTurn;
import chess.domain.board.Board;
import chess.domain.board.InitialBoardGenerator;
import chess.domain.board.SavedBoardGenerator;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Square;
import chess.dto.ChessGameRequest;
import chess.dto.GameIdResponse;
import chess.dto.MovementRequest;
import chess.dto.ScoreResponse;

@Service
public class ChessService {
    private final ChessGameDao chessGameDao;
    private final PieceDao pieceDao;

    public ChessService(ChessGameDao chessGameDao, PieceDao pieceDao) {
        this.chessGameDao = chessGameDao;
        this.pieceDao = pieceDao;
    }

    public Map<String, String> getEmojis(ChessGameRequest chessGameRequest, String restart) {
        ChessGame chessGame = loadChessGame(chessGameRequest, restart);
        return chessGame.getEmojis();
    }

    public Map<String, String> getSavedEmojis(ChessGameRequest chessGameRequest) {
        ChessGame chessGame = loadSavedChessGame(chessGameRequest.getGameId());
        return chessGame.getEmojis();
    }

    private ChessGame loadChessGame(ChessGameRequest chessGameRequest, String restart) {
        String gameId = chessGameRequest.getGameId();
        try {
            pieceDao.findByGameId(gameId);
        } catch (IllegalArgumentException e) {
            initBoard(gameId);
        }
        if ("true".equals(restart)) {
            initBoard(gameId);
        }
        loadTurn(gameId, restart);
        return loadGame(chessGameRequest);
    }

    private void initBoard(String gameId) {
        pieceDao.deleteByGameId(gameId);
        pieceDao.initPieces(gameId);
    }

    private void loadTurn(String gameId, String restart) {
        if ("true".equals(restart)) {
            chessGameDao.initTurn(gameId);
        }
    }

    private ChessGame loadGame(ChessGameRequest chessGameRequest) {
        String gameId = chessGameRequest.getGameId();
        ChessGame chessGame;
        try {
            GameTurn gameTurn = getTurn(gameId);
            checkCanContinue(gameTurn);
            chessGame = loadSavedChessGame(gameId);
        } catch (RuntimeException e) {
            chessGame = loadNewChessGame();
            startGame(gameId, chessGameRequest.getPassword(), chessGame);
        }
        return chessGame;
    }

    public GameTurn getTurn(String gameId) {
        return GameTurn.find(chessGameDao.findTurnById(gameId));
    }

    private void checkCanContinue(GameTurn gameTurn) {
        if (GameTurn.FINISHED.equals(gameTurn)) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isKingDie(ChessGameRequest chessGameRequest) {
        ChessGame chessGame = loadSavedChessGame(chessGameRequest.getGameId());
        return chessGame.isKingDie();
    }

    private ChessGame loadSavedChessGame(String gameId) {
        Map<Square, Piece> board = pieceDao.findByGameId(gameId);
        return new ChessGame(new SavedBoardGenerator(board), getTurn(gameId));
    }

    private ChessGame loadNewChessGame() {
        return new ChessGame(new InitialBoardGenerator(), GameTurn.READY);
    }

    private void startGame(String gameId, String password, ChessGame chessGame) {
        chessGameDao.save(gameId, password, chessGame);
        updateTurn(gameId, chessGame);
    }

    public void movePiece(ChessGameRequest chessGameRequest, MovementRequest movementRequest) {
        String source = movementRequest.getSource();
        String target = movementRequest.getTarget();
        String gameId = chessGameRequest.getGameId();

        ChessGame chessGame = loadSavedChessGame(gameId);
        chessGame.move(new Square(source), new Square(target));

        updateTurn(gameId, chessGame);
        updatePosition(gameId, source, target);
    }

    private void updateTurn(String gameId, ChessGame chessGame) {
        chessGameDao.updateTurn(gameId, chessGame);
    }

    private void updatePosition(String gameId, String source, String target) {
        pieceDao.deleteByPosition(new Square(target), gameId);
        pieceDao.updatePosition(new Square(source), new Square(target), gameId);
        pieceDao.insertNone(gameId, new Square(source));
    }

    public List<GameIdResponse> getGameIds() {
        return chessGameDao.findAllGame().stream()
                .map(GameIdResponse::new)
                .collect(Collectors.toList());
    }

    public void deleteGameByGameId(ChessGameRequest chessGameRequest) {
        String gameId = chessGameRequest.getGameId();
        String password = chessGameRequest.getPassword();
        checkPassword(gameId, password);
        checkCanDelete(GameTurn.find(chessGameDao.findTurnById(gameId)));
        chessGameDao.deleteByGameId(gameId, password);
        pieceDao.deleteByGameId(gameId);
    }

    private void checkPassword(String gameId, String password) {
        if (!chessGameDao.findPasswordByGameId(gameId, password)) {
            throw new IllegalArgumentException("비밀 번호 틀렸지롱~ 🤪");
        }
    }

    private void checkCanDelete(GameTurn gameTurn) {
        if (!GameTurn.FINISHED.equals(gameTurn)) {
            throw new IllegalArgumentException("아직 게임이 진행 중이라구!! 😡");
        }
    }

    public boolean isValidPassword(ChessGameRequest chessGameRequest) {
        return chessGameDao.findPasswordByGameId(chessGameRequest.getGameId(), chessGameRequest.getPassword());
    }

    public boolean isGameExist(ChessGameRequest chessGameRequest) {
        try {
            chessGameDao.findTurnById(chessGameRequest.getGameId());
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        }
    }

    public ScoreResponse calculateScore(ChessGameRequest chessGameRequest) {
        try {
            double whiteScore = getSavedGameResult(chessGameRequest.getGameId()).calculateScore(Color.WHITE);
            double blackScore = getSavedGameResult(chessGameRequest.getGameId()).calculateScore(Color.BLACK);
            return new ScoreResponse(whiteScore, blackScore);
        } catch (IllegalArgumentException e) {
            double whiteScore =  createGameResult().calculateScore(Color.WHITE);
            double blackScore = createGameResult().calculateScore(Color.BLACK);
            return new ScoreResponse(whiteScore, blackScore);
        }
    }

    private GameResult createGameResult() {
        Board board = new Board(new InitialBoardGenerator());
        return new GameResult(board);
    }

    private GameResult getSavedGameResult(String gameId) {
        Board board = new Board(new SavedBoardGenerator(pieceDao.findByGameId(gameId)));
        return new GameResult(board);
    }
}
