package chess.service;

import chess.controller.dto.GameDto;
import chess.controller.dto.request.MoveRequest;
import chess.controller.dto.response.ChessGameResponse;
import chess.controller.dto.response.PieceResponse;
import chess.controller.dto.response.StatusResponse;
import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.ChessGame;
import chess.domain.GameState;
import chess.domain.board.Board;
import chess.domain.board.Column;
import chess.domain.board.Position;
import chess.domain.board.Row;
import chess.domain.board.strategy.CreateCompleteBoardStrategy;
import chess.domain.piece.Piece;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private static final int ROW_INDEX = 0;
    private static final int COLUMN_INDEX = 1;

    private static final String NOT_HAVE_GAME = "해당하는 게임이 없습니다.";

    private final GameDao gameDao;
    private final PieceDao pieceDao;

    private final Map<Long, ChessGame> chessGames;

    public ChessService(GameDao gameDao, PieceDao pieceDao) {
        this.gameDao = gameDao;
        this.pieceDao = pieceDao;
        this.chessGames = new HashMap<>();
    }

    public int getGameId(String name, String password) {
        Optional<Integer> gameId = gameDao.find(name, password);
        if (gameId.isEmpty()) {
            gameDao.save(name, password);
            final int newGameId = gameDao.find(name, password).get();
            saveBoard(newGameId, (new CreateCompleteBoardStrategy()).createPieces());
            return newGameId;
        }
        return gameId.get();
    }

    public ChessGameResponse loadGame(long gameId) {
        Optional<GameState> maybeGameState = gameDao.load(gameId);
<<<<<<< HEAD
        GameState gameState = maybeGameState.orElseThrow(NoSuchElementException::new);
=======
        if (maybeGameState.isEmpty()) {
            throw new IllegalArgumentException("게임이 없습니다.");
        }
        GameState gameState = maybeGameState.get();
>>>>>>> refactor: 체스 보드를 만드는 메서드 분리
        Board board = createBoard(gameId);
        chessGames.put(gameId, new ChessGame(board, gameState));
        return new ChessGameResponse(getChessGame(gameId));
    }

    private Board createBoard(long gameId) {
        Map<Position, Piece> pieces = new HashMap<>();
        for (PieceResponse pieceResponse : pieceDao.findAll(gameId)) {
            Position position = parseStringToPosition(pieceResponse.getPosition());
            Piece piece = pieceResponse.toPiece();
            pieces.put(position, piece);
        }
        return new Board(() -> pieces);
    }

    private ChessGame getChessGame(long gameId) {
        if (!chessGames.containsKey(gameId)) {
            throw new IllegalArgumentException(NOT_HAVE_GAME);
        }
        return chessGames.get(gameId);
    }

    public ChessGameResponse createGame(long gameId) {
        ChessGame chessGame = new ChessGame(new Board(new CreateCompleteBoardStrategy()));
        saveBoard(gameId, chessGame.getBoard());
        chessGames.put(gameId, chessGame);
        return new ChessGameResponse(chessGame);
    }

    public ChessGameResponse restartGame(long gameId) {
        gameDao.updateState(gameId, GameState.READY);
        return createGame(gameId);
    }

    public void saveBoard(long gameId, Map<Position, Piece> board) {
        board.forEach((position, piece) -> savePiece(gameId, position, piece));
    }

    public void savePiece(long gameId, Position position, Piece piece) {
        if (pieceDao.find(gameId, position).isEmpty()) {
            pieceDao.save(gameId, position, piece);
        }
    }

    public ChessGameResponse startOrRestartGame(long gameId) {
        Optional<GameState> maybeGameState = gameDao.load(gameId);
        GameState gameState = maybeGameState.orElseThrow(() -> new IllegalArgumentException("게임이 없습니다."));
        if (gameState == GameState.READY) {
            return startGame(gameId);
        }
        return restartGame(gameId);
    }

    public ChessGameResponse startGame(long gameId) {
        ChessGame chessGame = getChessGame(gameId);
        chessGame.start();
        gameDao.updateState(gameId, chessGame.getGameState());
        return new ChessGameResponse(chessGame);
    }

    public ChessGameResponse move(long gameId, MoveRequest moveRequest) {
        ChessGame chessGame = getChessGame(gameId);
        Position start = parseStringToPosition(moveRequest.getStart());
        Position target = parseStringToPosition(moveRequest.getTarget());
        chessGame.move(start, target);
        if (pieceDao.find(gameId, target).isPresent()) {
            pieceDao.delete(gameId, target);
        }
        pieceDao.updatePosition(gameId, start, target);
        gameDao.updateState(gameId, chessGame.getGameState());
        return new ChessGameResponse(chessGame);
    }

    public StatusResponse status(long gameId) {
        ChessGame chessGame = getChessGame(gameId);
        return new StatusResponse(chessGame.createStatus());
    }

    public ChessGameResponse end(long gameId) {
        ChessGame chessGame = getChessGame(gameId);
        chessGame.end();
        gameDao.updateState(gameId, chessGame.getGameState());
        return new ChessGameResponse(chessGame);
    }

    public boolean deleteGameAfterCheckingPassword(long gameId, String password) {
        if (isFulfillDeleteCondition(gameId, password)) {
            gameDao.delete(gameId);
            return true;
        }
        return false;
    }

    private boolean isFulfillDeleteCondition(long gameId, String password) {
        Optional<GameState> gameState = gameDao.load(gameId);
        return gameDao.findPassword(gameId).equals(password)
                && gameState.isPresent()
                && gameState.get() == GameState.FINISHED;
    }

    private Position parseStringToPosition(final String rawPosition) {
        final String[] separatedPosition = rawPosition.split("");
        final Column column = Column.from(separatedPosition[ROW_INDEX]);
        final Row row = Row.from(separatedPosition[COLUMN_INDEX]);
        return new Position(column, row);
    }

    public List<GameDto> findAllGames() {
        return gameDao.findAll();
    }

    public boolean checkPassword(long gameId, String password) {
        return password.equals(gameDao.findPassword(gameId));
    }
}
