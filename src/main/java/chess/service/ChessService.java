package chess.service;

import chess.controller.dto.request.CreateGameRequest;
import chess.controller.dto.request.MoveRequest;
import chess.controller.dto.response.ChessGameResponse;
import chess.controller.dto.response.ChessGamesResponse;
import chess.controller.dto.response.GameRoomResponse;
import chess.controller.dto.response.PieceResponse;
import chess.controller.dto.response.StatusResponse;
import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.dao.entity.GameEntity;
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
import java.util.stream.Collectors;
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

    public Long createGame(CreateGameRequest createGameRequest) {
        String salt = PasswordEncryptor.generateSalt();
        GameEntity gameEntity = GameEntity.toSave(createGameRequest.getGameName(),
                PasswordEncryptor.encrypt(createGameRequest.getPassword(), salt), salt,
                GameState.READY);
        ChessGame chessGame = new ChessGame(new Board(new CreateCompleteBoardStrategy()));
        Long gameId = gameDao.save(gameEntity);
        saveBoard(gameId, chessGame.getBoard());
        return gameId;
    }
    
    public void saveBoard(Long gameId, Map<Position, Piece> board) {
        board.forEach((position, piece) -> savePiece(gameId, position, piece));
    }

    private void savePiece(Long gameId, Position position, Piece piece) {
        pieceDao.save(gameId, position, piece);
    }

    public ChessGamesResponse loadAllGames() {
        return new ChessGamesResponse(gameDao.findAll()
                .stream()
                .map(entity -> new GameRoomResponse(entity.getId(), entity.getName()))
                .collect(Collectors.toList()));
    }

    public ChessGameResponse loadGame(Long gameId) {
        return new ChessGameResponse(createChessGameObject(gameId));
    }

    private ChessGame createChessGameObject(Long gameId) {
        GameEntity gameEntity = gameDao.findById(gameId);
        GameState gameState = gameEntity.getState();
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
        pieceDao.deleteByGameId(gameId);
        gameDao.updateState(gameId, GameState.READY);
        Board board = new Board(new CreateCompleteBoardStrategy());
        saveBoard(gameId, board.getBoard());

        return new ChessGameResponse(new ChessGame(board));
    }

    public ChessGameResponse move(Long gameId, MoveRequest moveRequest) {
        ChessGame chessGame = createChessGameObject(gameId);
        Position start = parseStringToPosition(moveRequest.getStart());
        Position target = parseStringToPosition(moveRequest.getTarget());
        chessGame.move(start, target);
        if (pieceDao.find(gameId, target).isPresent()) {
            pieceDao.deleteByGameIdAndPosition(gameId, target);
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
        GameEntity gameEntity = gameDao.findById(gameId);
        authenticate(password, gameEntity);
        validateDeletable(gameEntity);
        gameDao.delete(gameId);
    }

    private void authenticate(String password, GameEntity gameEntity) {
        String actualPassword = gameEntity.getPassword();
        String salt = gameEntity.getSalt();
        String sentPassword = PasswordEncryptor.encrypt(password, salt);
        if (!sentPassword.equals(actualPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private void validateDeletable(GameEntity gameEntity) {
        if (!gameEntity.getState().isFinished()) {
            throw new IllegalArgumentException("게임이 종료되기 전에는 삭제할 수 없습니다.");
        }
    }

    private Position parseStringToPosition(final String rawPosition) {
        final String[] separatedPosition = rawPosition.split("");
        final Column column = Column.from(separatedPosition[ROW_INDEX]);
        final Row row = Row.from(separatedPosition[COLUMN_INDEX]);
        return new Position(column, row);
    }
}
