package chess.service;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.game.ChessGame;
import chess.domain.game.GameResult;
import chess.domain.piece.ChessmenInitializer;
import chess.domain.piece.Pieces;
import chess.dto.GameResultDto;
import chess.dto.LogInDto;
import chess.dto.MoveDto;
import chess.entity.RoomEntity;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {
    private static final String SAME_NAME_ROOM_ERROR_MESSAGE = "이미 해당 이름의 방이 있습니다.";
    private static final String PLAYING_CHESS_ERROR_MESSAGE = "진행중인 체스방은 삭제할 수 없습니다.";

    private final ChessmenInitializer chessmenInitializer = new ChessmenInitializer();

    private final PieceDao pieceDao;
    private final GameDao gameDao;

    public ChessGameService(PieceDao pieceDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
    }

    public void createGame(LogInDto logInDto) {
        validateUniqueId(logInDto);
        initGame(logInDto);
    }

    private void validateUniqueId(LogInDto logInDto) {
        if (gameDao.isInId(logInDto.getGameId())) {
            throw new IllegalArgumentException(SAME_NAME_ROOM_ERROR_MESSAGE);
        }
    }

    public void validateLogIn(LogInDto logInDto) {
        RoomEntity roomEntity = gameDao.findRoom(logInDto);
        roomEntity.validateLogIn(logInDto);
    }

    private void initGame(LogInDto logInDto) {
        gameDao.create(logInDto);
        pieceDao.createAll(chessmenInitializer.init(), logInDto.getGameId());
    }

    public ChessGame getGameStatus(String gameId) {
        RoomEntity roomEntity = gameDao.findRoom(gameId);
        return new ChessGame(roomEntity.isForce_end_flag(), pieceDao.findAll(gameId),
                roomEntity.getTurn());
    }

    public Pieces getPieces(String gameId) {
        return getGameStatus(gameId).getChessmen();
    }

    public GameResultDto calculateGameResult(String gameId) {
        return new GameResultDto(GameResult.calculate(getGameStatus(gameId).getChessmen()));
    }

    public void cleanGame(LogInDto logInDto) {
        validateLogIn(logInDto);
        pieceDao.deleteAll(logInDto.getGameId());
        gameDao.delete(logInDto.getGameId());
    }

    public void move(String gameId, MoveDto moveDto) {
        ChessGame chessGame = getGameStatus(gameId);
        chessGame.moveChessmen(moveDto.toEntity());
        pieceDao.deleteAll(gameId);
        pieceDao.createAll(chessGame.getChessmen(), gameId);
        gameDao.updateTurn(chessGame.getTurn(), gameId);
        gameDao.updateForceEndFlag(chessGame.getForceEndFlag(), gameId);
    }

    public List<RoomEntity> getRooms() {
        return gameDao.findAllGame();
    }

    public void changeToEnd(String gameId) {
        gameDao.updateForceEndFlag(true, gameId);
    }

    public void validateEnd(String gameId) {
        if (!gameDao.findForceEndFlag(gameId)) {
            throw new IllegalArgumentException(PLAYING_CHESS_ERROR_MESSAGE);
        }
    }
}
