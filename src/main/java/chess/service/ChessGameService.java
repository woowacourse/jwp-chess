package chess.service;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.command.MoveCommand;
import chess.domain.game.ChessGame;
import chess.domain.game.GameResult;
import chess.domain.game.LogIn;
import chess.domain.game.Room;
import chess.domain.piece.ChessmenInitializer;
import chess.domain.piece.Pieces;
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

    public void createGame(LogIn logIn) {
        validateUniqueId(logIn);
        logIn.validate();
        gameDao.create(logIn);
        pieceDao.createAll(chessmenInitializer.init(), logIn.getId());
    }

    public void validateLogIn(LogIn logIn) {
        gameDao.findRoom(logIn.getId()).validateLogInPassword(logIn);
    }

    private void validateUniqueId(LogIn logIn) {
        try {
            gameDao.findNoPasswordRoom(logIn.getId());
        } catch (IllegalArgumentException e) {
            return;
        }
        throw new IllegalArgumentException(ALREADY_ROOM_ID_EXIST_ERROR_MESSAGE);
    }

    public ChessGame getGameStatus(String id) {
        return new ChessGame(gameDao.findNoPasswordRoom(id), pieceDao.findAll(id));
    }

    public Pieces getPieces(String id) {
        return getGameStatus(id).getChessmen();
    }

    public GameResult calculateGameResult(String id) {
        return GameResult.calculate(getGameStatus(id).getChessmen());
    }

    public void cleanGame(LogIn logIn) {
        validateLogIn(logIn);
        pieceDao.deleteAll(logIn.getId());
        gameDao.delete(logIn.getId());
    }

    public void move(String id, MoveCommand moveCommand) {
        ChessGame chessGame = getGameStatus(id);
        chessGame.moveChessmen(moveCommand);
        pieceDao.deleteAll(id);
        pieceDao.createAll(chessGame.getChessmen(), id);
        gameDao.updateTurn(chessGame.getTurn(), id);
        gameDao.updateForceEndFlag(chessGame.getEnd(), id);
    }

    public List<Room> getRooms() {
        return gameDao.findAllRoom();
    }

    public void changeToEnd(String id) {
        gameDao.updateForceEndFlag(true, id);
    }

    public void validateEnd(String id) {
        if (!gameDao.findNoPasswordRoom(id).isEnd()) {
            throw new IllegalArgumentException(PLAYING_CHESS_ERROR_MESSAGE);
        }
    }
}
