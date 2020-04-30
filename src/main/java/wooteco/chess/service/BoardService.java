package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.database.dao.ChessDao;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.strategy.NormalInitStrategy;

import java.sql.SQLException;

@Service
public class BoardService {
    private ChessDao chessDao;

    public BoardService(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    public void init(int roomId) throws SQLException {
        NormalInitStrategy normalInitStrategy = new NormalInitStrategy();
        Board board = new Board(normalInitStrategy.init());
        chessDao.save(roomId, board);
    }

    public void play(int roomId, String source, String target) throws SQLException {
        Board board = chessDao.load(roomId);
        board.moveIfPossible(Position.of(source), Position.of(target));
        chessDao.save(roomId, board);
    }

    public boolean isFinished(int roomId) throws SQLException {
        Board board = chessDao.load(roomId);
        return board.isFinished();
    }

    public boolean isTurnWhite(int roomId) throws SQLException {
        Board board = chessDao.load(roomId);
        return board.getTurn() == Team.WHITE;
    }

    public Board getBoard(int roomId) throws SQLException {
        return chessDao.load(roomId);
    }
}
