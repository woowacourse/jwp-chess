package wooteco.chess.service;

import wooteco.chess.database.dao.ChessDao;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.strategy.NormalInitStrategy;

import java.sql.SQLException;

public class RoomService {
    private ChessDao chessDao = new ChessDao();

    public void create() throws SQLException {
        NormalInitStrategy strategy = new NormalInitStrategy();
        Board board = new Board(strategy.init());

        chessDao.createRoom(board);
    }

    public void delete(int roomId) throws SQLException {
        chessDao.delete(roomId);
    }
}
