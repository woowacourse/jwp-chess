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
import chess.domain.position.Square;
import chess.dto.ChessGameDto;
import chess.dto.MovementRequest;

@Service
public class ChessService {
    private final ChessGameDao chessGameDao;
    private final PieceDao pieceDao;

    public ChessService(ChessGameDao chessGameDao, PieceDao pieceDao) {
        this.chessGameDao = chessGameDao;
        this.pieceDao = pieceDao;
    }

    public Map<String, String> getEmojis(ChessGameDto chessGameDto, String restart) {
        ChessGame chessGame = loadChessGame(chessGameDto, restart);
        return chessGame.getEmojis();
    }

    public Map<String, String> getSavedEmojis(ChessGameDto chessGameDto) {
        ChessGame chessGame = loadSavedChessGame(chessGameDto);
        return chessGame.getEmojis();
    }

    private ChessGame loadChessGame(ChessGameDto chessGameDto, String restart) {
        String gameID = chessGameDto.getGameID();
        try {
            pieceDao.findByGameID(gameID);
        } catch (IllegalArgumentException e) {
            initBoard(gameID);
        }
        if ("true".equals(restart)) {
            initBoard(gameID);
        }
        loadTurn(gameID, restart);
        return loadGame(chessGameDto);
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

    private ChessGame loadGame(ChessGameDto chessGameDto) {
        String gameID = chessGameDto.getGameID();
        ChessGame chessGame;
        try {
            GameTurn gameTurn = getTurn(chessGameDto);
            checkCanContinue(gameTurn);
            chessGame = loadSavedChessGame(chessGameDto);
        } catch (RuntimeException e) {
            chessGame = loadNewChessGame();
            startGame(gameID, chessGameDto.getPassword(), chessGame);
        }
        return chessGame;
    }

    public GameTurn getTurn(ChessGameDto chessGameDto) {
        return GameTurn.find(chessGameDao.findTurnByID(chessGameDto.getGameID()));
    }

    private void checkCanContinue(GameTurn gameTurn) {
        if (GameTurn.FINISHED.equals(gameTurn)) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isKingDie(ChessGameDto chessGameDto) {
        ChessGame chessGame = loadSavedChessGame(chessGameDto);
        return chessGame.isKingDie();
    }

    private ChessGame loadSavedChessGame(ChessGameDto chessGameDto) {
        return new ChessGame(new SavedBoardGenerator(pieceDao.findByGameID(chessGameDto.getGameID())),
                getTurn(chessGameDto));
    }

    private ChessGame loadNewChessGame() {
        return new ChessGame(new InitialBoardGenerator(), GameTurn.READY);
    }

    private void startGame(String gameID, String password, ChessGame chessGame) {
        chessGameDao.save(gameID, password, chessGame);
        updateTurn(gameID, chessGame);
    }

    public void movePiece(ChessGameDto chessGameDto, MovementRequest movementRequest) {
        String source = movementRequest.getSource();
        String target = movementRequest.getTarget();
        ChessGame chessGame = loadSavedChessGame(chessGameDto);
        chessGame.move(new Square(source), new Square(target));

        String gameID = chessGameDto.getGameID();
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

    public List<ChessGameDto> getGameIDs() {
        return chessGameDao.findAllGame().stream()
                .map(gameID -> new ChessGameDto(gameID, null))
                .collect(Collectors.toList());
    }

    public void deleteGameByGameID(ChessGameDto chessGameDto) {
        String gameID = chessGameDto.getGameID();
        String password = chessGameDto.getPassword();
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

    public boolean isValidPassword(ChessGameDto chessGameDto) {
        return chessGameDao.findPasswordByGameID(chessGameDto.getGameID(), chessGameDto.getPassword());
    }

    public boolean isGameExist(ChessGameDto chessGameDto) {
        try {
            chessGameDao.findTurnByID(chessGameDto.getGameID());
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        }
    }

    public double calculateScore(ChessGameDto chessGameDto, Color color) {
        try {
            return getSavedGameResult(chessGameDto).calculateScore(color);
        } catch (IllegalArgumentException e) {
            return getGameResult().calculateScore(color);
        }
    }

    private GameResult getGameResult() {
        Board board = new Board(new InitialBoardGenerator());
        return new GameResult(board);
    }

    private GameResult getSavedGameResult(ChessGameDto chessGameDto) {
        Board board = new Board(new SavedBoardGenerator(pieceDao.findByGameID(chessGameDto.getGameID())));
        return new GameResult(board);
    }
}
