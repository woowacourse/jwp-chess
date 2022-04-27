package chess.service;

import chess.controller.dto.request.CreateGameRequest;
import chess.controller.dto.request.MoveRequest;
import chess.controller.dto.response.ChessGameResponse;
import chess.controller.dto.response.ChessGamesResponse;
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
import chess.util.PasswordEncryptor;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChessService {

    private static final int ROW_INDEX = 0;
    private static final int COLUMN_INDEX = 1;

    private final GameDao gameDao;
    private final PieceDao pieceDao;

    public ChessService(GameDao gameDao, PieceDao pieceDao) {
        this.gameDao = gameDao;
        this.pieceDao = pieceDao;
    }

    public ChessGameResponse createGame(Long gameId, CreateGameRequest createGameRequest) {
        return createGame(gameId, createGameRequest.getGameName(), createGameRequest.getPassword());
    }

    private ChessGameResponse createGame(Long gameId, String gameName, String password) {
        String salt = PasswordEncryptor.generateSalt();
        ChessGame chessGame = new ChessGame(new Board(new CreateCompleteBoardStrategy()));
        gameDao.save(gameId, gameName, PasswordEncryptor.encrypt(password, salt), salt);
        saveBoard(gameId, chessGame.getBoard());
        return new ChessGameResponse(chessGame);
    }

    public void saveBoard(Long gameId, Map<Position, Piece> board) {
        board.forEach((position, piece) -> savePiece(gameId, position, piece));
    }

    private void savePiece(Long gameId, Position position, Piece piece) {
        if (pieceDao.find(gameId, position).isEmpty()) {
            pieceDao.save(gameId, position, piece);
        }
    }

    public ChessGamesResponse findAllGameIds() {
        return new ChessGamesResponse(gameDao.findAllGames());
    }

    public ChessGameResponse loadGame(Long gameId) {
        return new ChessGameResponse(createChessGameObject(gameId));
    }

    private ChessGame createChessGameObject(Long gameId) {
        Optional<GameState> maybeGameState = gameDao.findState(gameId);
        GameState gameState = maybeGameState.orElseThrow(NoSuchElementException::new);
        Board board = createBoard(gameId);
        return new ChessGame(board, gameState);
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

    public ChessGameResponse startGame(Long gameId) {
        gameDao.updateState(gameId, GameState.WHITE_RUNNING);
        return loadGame(gameId);
    }

    public ChessGameResponse resetGame(Long gameId) {
        Optional<String> maybeGameName = gameDao.findName(gameId);
        String gameName = maybeGameName.orElseThrow(NoSuchElementException::new);
        Optional<String> maybePassword = gameDao.findPassword(gameId);
        String password = maybePassword.orElseThrow(NoSuchElementException::new);
        gameDao.delete(gameId);
        return createGame(gameId, gameName, password);
    }

    public ChessGameResponse move(Long gameId, MoveRequest moveRequest) {
        ChessGame chessGame = createChessGameObject(gameId);
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

    public StatusResponse status(Long gameId) {
        ChessGame chessGame = createChessGameObject(gameId);
        return new StatusResponse(chessGame.createStatus());
    }

    public void deleteGame(Long gameId, String password) {
        authenticate(gameId, password);
        Optional<GameState> maybeGameState = gameDao.findState(gameId);
        GameState gameState = maybeGameState.orElseThrow(NoSuchElementException::new);
        if (!gameState.isFinished()) {
            throw new IllegalArgumentException("게임이 종료되기 전에는 삭제할 수 없습니다.");
        }
        gameDao.delete(gameId);
    }

    private void authenticate(Long gameId, String password) {
        Optional<String> maybeSalt = gameDao.findSalt(gameId);
        String salt = maybeSalt.orElseThrow(NoSuchElementException::new);
        Optional<String> maybePassword = gameDao.findPassword(gameId);
        String actualPassword = maybePassword.orElseThrow(NoSuchElementException::new);
        String sentPassword = PasswordEncryptor.encrypt(password, salt);
        if (!sentPassword.equals(actualPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private Position parseStringToPosition(final String rawPosition) {
        final String[] separatedPosition = rawPosition.split("");
        final Column column = Column.from(separatedPosition[ROW_INDEX]);
        final Row row = Row.from(separatedPosition[COLUMN_INDEX]);
        return new Position(column, row);
    }
}
