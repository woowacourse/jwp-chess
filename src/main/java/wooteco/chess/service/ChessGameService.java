package wooteco.chess.service;

import wooteco.chess.database.dao.IsFinishedDao;
import wooteco.chess.database.dao.PiecesDao;
import wooteco.chess.database.dao.TurnDao;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.strategy.NormalInitStrategy;

import java.sql.SQLException;

public class ChessGameService {
    private PiecesDao piecesDao;
    private TurnDao turnDao;
    private IsFinishedDao isFinishedDao;

    public ChessGameService(PiecesDao piecesDao, TurnDao turnDao, IsFinishedDao isFinishedDao) {
        this.piecesDao = piecesDao;
        this.turnDao = turnDao;
        this.isFinishedDao = isFinishedDao;
    }

    public void init() throws SQLException {
        NormalInitStrategy normalInitStrategy = new NormalInitStrategy();
        piecesDao.save(normalInitStrategy.init());
        turnDao.save(Team.WHITE);
        isFinishedDao.save(false);
    }

    public void play(String source, String target) throws SQLException {
        Board board = new Board(piecesDao.load(), turnDao.load());
        board.moveIfPossible(Position.of(source), Position.of(target));
        piecesDao.save(board.getPieces());
        turnDao.save(board.getTurn());
        isFinishedDao.save(board.isFinished());
    }

    public void delete() throws SQLException {
        piecesDao.delete();
        turnDao.delete();
        isFinishedDao.delete();
    }

    public boolean isEmpty() throws SQLException {
        return piecesDao.load() == null || turnDao.load() == null;
    }

    public boolean isFinished() throws SQLException {
        Board board = new Board(piecesDao.load(), turnDao.load(), isFinishedDao.load());
        return board.isFinished();
    }

    public boolean isTurnWhite() throws SQLException {
        Board board = new Board(piecesDao.load(), turnDao.load());
        return board.getTurn() == Team.WHITE;
    }

    public Board getBoard() throws SQLException {
        return new Board(piecesDao.load(), turnDao.load());
    }
}
