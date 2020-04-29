package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.database.dao.ChessDao;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.strategy.NormalInitStrategy;

import java.sql.SQLException;
import java.util.List;

@Service
public class RoomService {
    private ChessDao chessDao = new ChessDao();

    public List<Integer> loadRoomNumbers() throws SQLException {
        return chessDao.loadRoomNumbers();
    }

    public int create() throws SQLException {
        NormalInitStrategy strategy = new NormalInitStrategy();
        Board board = new Board(strategy.init());

        return chessDao.createRoom(board);
    }

    public void delete(int roomId) throws SQLException {
        chessDao.delete(roomId);
    }
}
