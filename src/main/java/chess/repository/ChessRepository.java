package chess.repository;

import chess.repository.dao.BoardDao;
import chess.repository.dao.ChessGameDao;
import chess.repository.dao.GameRoomDao;
import chess.repository.entity.BoardEntity;
import chess.repository.entity.ChessGameEntity;
import chess.repository.entity.GameRoomEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ChessRepository {

    private final GameRoomDao gameRoomDao;
    private final ChessGameDao chessGameDao;
    private final BoardDao boardDao;

    public ChessRepository(GameRoomDao gameRoomDao, ChessGameDao chessGameDao, BoardDao boardDao) {
        this.gameRoomDao = gameRoomDao;
        this.chessGameDao = chessGameDao;
        this.boardDao = boardDao;
    }

    public void save(final GameRoomEntity gameRoomEntity,
                     final ChessGameEntity chessGameEntity,
                     final List<BoardEntity> boardEntities) {
        gameRoomDao.save(gameRoomEntity);
        chessGameDao.save(chessGameEntity);
        boardDao.save(boardEntities);
    }

    public GameRoomEntity loadGameRoom(final String gameRoomId) {
        return gameRoomDao.load(gameRoomId);
    }

    public List<GameRoomEntity> loadAllGameRoom() {
        return gameRoomDao.loadAll();
    }

    public ChessGameEntity loadChessGame(final String gameRoomId) {
        return chessGameDao.load(gameRoomId);
    }

    public List<BoardEntity> loadBoard(final String gameRoomID) {
        return boardDao.load(gameRoomID);
    }

    public void updateChessGame(final ChessGameEntity chessGameEntity) {
        chessGameDao.update(chessGameEntity);
    }

    public void updateBoard(final BoardEntity boardEntity) {
        boardDao.update(boardEntity);
    }

    public void delete(final String gameRoomId, final String password) {
        gameRoomDao.delete(gameRoomId, password);
    }
}
