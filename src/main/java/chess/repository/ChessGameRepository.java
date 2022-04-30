package chess.repository;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.dto.CurrentStatusDto;
import chess.dto.MovingPositionDto;
import chess.piece.Piece;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ChessGameRepository {

    private final BoardDao boardDao;
    private final CurrentStatusDao currentStatusDao;
    private final GameDao gameDao;

    public ChessGameRepository(BoardDao boardDao, CurrentStatusDao currentStatusDao, GameDao gameDao) {
        this.boardDao = boardDao;
        this.currentStatusDao = currentStatusDao;
        this.gameDao = gameDao;
    }

    public boolean isDuplicateName(String name) {
        return gameDao.isDuplicateName(name);
    }

    public void saveNewGame(String name, String password, CurrentStatusDto currentStatus) {
        int gameId = gameDao.saveGame(name, password);
        currentStatusDao.save(gameId, currentStatus);
    }

    public void saveGame(int gameId, Map<Position, Piece> chessboard, CurrentStatusDto currentStatus) {
        boardDao.saveBoard(gameId, chessboard);
        currentStatusDao.update(gameId, currentStatus);
    }

    public void saveMove(int gameId, MovingPositionDto movingPosition, CurrentStatusDto currentStatus) {
        boardDao.saveMove(gameId, movingPosition);
        currentStatusDao.update(gameId, currentStatus);
    }

    public ChessGame find(int gameId) {
        return new ChessGame(currentStatusDao.findByGameId(gameId), boardDao.findByGameId(gameId));
    }

    public void saveEnd(int gameId, String state) {
        currentStatusDao.saveEnd(gameId,state);
    }
}
