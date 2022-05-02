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
        String gameId = chessGameDto.getGameId();
        try {
            pieceDao.findByGameId(gameId);
        } catch (IllegalArgumentException e) {
            initBoard(gameId);
        }
        if ("true".equals(restart)) {
            initBoard(gameId);
        }
        loadTurn(gameId, restart);
        return loadGame(chessGameDto);
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

    private ChessGame loadGame(ChessGameDto chessGameDto) {
        String gameId = chessGameDto.getGameId();
        ChessGame chessGame;
        try {
            GameTurn gameTurn = getTurn(chessGameDto);
            checkCanContinue(gameTurn);
            chessGame = loadSavedChessGame(chessGameDto);
        } catch (RuntimeException e) {
            chessGame = loadNewChessGame();
            startGame(gameId, chessGameDto.getPassword(), chessGame);
        }
        return chessGame;
    }

    public GameTurn getTurn(ChessGameDto chessGameDto) {
        return GameTurn.find(chessGameDao.findTurnById(chessGameDto.getGameId()));
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
        Map<Square, Piece> board = pieceDao.findByGameId(chessGameDto.getGameId());
        return new ChessGame(new SavedBoardGenerator(board), getTurn(chessGameDto));
    }

    private ChessGame loadNewChessGame() {
        return new ChessGame(new InitialBoardGenerator(), GameTurn.READY);
    }

    private void startGame(String gameId, String password, ChessGame chessGame) {
        chessGameDao.save(gameId, password, chessGame);
        updateTurn(gameId, chessGame);
    }

    public void movePiece(ChessGameDto chessGameDto, MovementRequest movementRequest) {
        String source = movementRequest.getSource();
        String target = movementRequest.getTarget();
        ChessGame chessGame = loadSavedChessGame(chessGameDto);
        chessGame.move(new Square(source), new Square(target));

        String gameId = chessGameDto.getGameId();
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

    public List<ChessGameDto> getGameIds() {
        return chessGameDao.findAllGame().stream()
                .map(gameId -> new ChessGameDto(gameId, null))
                .collect(Collectors.toList());
    }

    public void deleteGameByGameId(ChessGameDto chessGameDto) {
        String gameId = chessGameDto.getGameId();
        String password = chessGameDto.getPassword();
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

    public boolean isValidPassword(ChessGameDto chessGameDto) {
        return chessGameDao.findPasswordByGameId(chessGameDto.getGameId(), chessGameDto.getPassword());
    }

    public boolean isGameExist(ChessGameDto chessGameDto) {
        try {
            chessGameDao.findTurnById(chessGameDto.getGameId());
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        }
    }

    public double calculateScore(ChessGameDto chessGameDto, Color color) {
        try {
            return getSavedGameResult(chessGameDto).calculateScore(color);
        } catch (IllegalArgumentException e) {
            return createGameResult().calculateScore(color);
        }
    }

    private GameResult createGameResult() {
        Board board = new Board(new InitialBoardGenerator());
        return new GameResult(board);
    }

    private GameResult getSavedGameResult(ChessGameDto chessGameDto) {
        Board board = new Board(new SavedBoardGenerator(pieceDao.findByGameId(chessGameDto.getGameId())));
        return new GameResult(board);
    }
}
