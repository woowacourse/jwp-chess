package wooteco.chess.service;

import wooteco.chess.database.dao.ChessDao;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.strategy.NormalInitStrategy;

import java.sql.SQLException;

public class BoardService {
    private int roomId;
    private ChessDao chessDao;

    public BoardService(int roomId, ChessDao chessDao) {
        this.roomId = roomId;
        this.chessDao = chessDao;
    }

    public void init() throws SQLException {
        NormalInitStrategy normalInitStrategy = new NormalInitStrategy();
        Board board = new Board(normalInitStrategy.init());
        chessDao.save(roomId, board);
    }

    public void play(String source, String target) throws SQLException {
        Board board = chessDao.load(roomId);
        board.moveIfPossible(Position.of(source), Position.of(target));
        chessDao.save(roomId, board);
    }

    public boolean isFinished() throws SQLException {
        Board board = chessDao.load(roomId);
        return board.isFinished();
    }

    public boolean isTurnWhite() throws SQLException {
        Board board = chessDao.load(roomId);
        return board.getTurn() == Team.WHITE;
    }

    public Board getBoard() throws SQLException {
        return chessDao.load(roomId);
    }
}
