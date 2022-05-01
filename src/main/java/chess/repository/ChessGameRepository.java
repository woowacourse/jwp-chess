package chess.repository;

import chess.dao.BoardDao;
import chess.dao.CurrentStatusDao;
import chess.dao.GameDao;
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

    public Map<Long, String> findGameList() {
        return gameDao.findGameList();
    }

    public String findPasswordById(long gameId) {
        return gameDao.findPasswordById(gameId);
    }

    public void delete(long gameId) {
        gameDao.delete(gameId);
    }

    public void saveNewGame(String name, String password, CurrentStatusDto currentStatus) {
        long gameId = gameDao.saveGame(name, password);
        currentStatusDao.save(gameId, currentStatus);
    }

    public void saveGame(long gameId, Map<Position, Piece> chessboard, CurrentStatusDto currentStatus) {
        boardDao.saveBoard(gameId, chessboard);
        currentStatusDao.update(gameId, currentStatus);
    }

    public void saveMove(long gameId, MovingPositionDto movingPosition, CurrentStatusDto currentStatus) {
        boardDao.saveMove(gameId, movingPosition);
        currentStatusDao.update(gameId, currentStatus);
    }

    public ChessGame find(long gameId) {
        return new ChessGame(currentStatusDao.findByGameId(gameId), boardDao.findByGameId(gameId));
    }

    public void saveState(long gameId, String state) {
        currentStatusDao.saveState(gameId, state);
    }

}
