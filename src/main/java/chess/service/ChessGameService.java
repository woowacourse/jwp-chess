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
import chess.domain.game.Room;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {
    private static final String PLAYING_CHESS_ERROR_MESSAGE = "진행중인 체스방은 삭제할 수 없습니다.";
    private static final String ALREADY_ROOM_ID_EXIST_ERROR_MESSAGE = "이미 해당 이름의 방이 있습니다.";

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

    public void validateLogIn(LogInDto logInDto) {
        gameDao.findRoom(logInDto.getGameId()).validateLogInPassword(logInDto);
    }

    private void validateUniqueId(LogInDto logInDto) {
        try {
            gameDao.findNoPasswordRoom(logInDto.getGameId());
        } catch (IllegalArgumentException e) {
            return;
        }
        throw new IllegalArgumentException(ALREADY_ROOM_ID_EXIST_ERROR_MESSAGE);
    }

    private void initGame(LogInDto logInDto) {
        gameDao.create(logInDto);
        pieceDao.createAll(chessmenInitializer.init(), logInDto.getGameId());
    }

    public ChessGame getGameStatus(String gameId) {
        Room room = gameDao.findNoPasswordRoom(gameId);
        return new ChessGame(room.isEnd(), pieceDao.findAll(gameId),
                room.getTurn());
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
        gameDao.updateForceEndFlag(chessGame.getEnd(), gameId);
    }

    public List<Room> getRooms() {
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
