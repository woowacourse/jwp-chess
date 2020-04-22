package wooteco.chess.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.player.User;
import wooteco.chess.util.DBConnector;

public class BoardDAO {

    private CellDAO cellDAO;
    private DBConnector dbConnector;

    public BoardDAO(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
        this.cellDAO = new CellDAO(dbConnector);
    }

    public void addBoard(Board board, User blackUser, User whiteUser, int turn) throws SQLException {
        dbConnector.executeUpdate("INSERT INTO gameinfo (black, white, turn) VALUES (?, ?, ?)", blackUser.getName(),
                whiteUser.getName(), String.valueOf(turn));

        ResultSet rs = findBoardResultSet(blackUser, whiteUser);

        if (!rs.next())
            return;

        int gameInfoId = rs.getInt(1);

        cellDAO.addCells(board, gameInfoId);
    }

    public Optional<Board> findBoardByUser(User blackUser, User whiteUser) throws SQLException {
        ResultSet rs = findBoardResultSet(blackUser, whiteUser);

        if (!rs.next())
            return Optional.empty();

        int gameInfoId = rs.getInt(1);

        return Optional.ofNullable(BoardFactory.of(cellDAO.findCellsByBoardId(gameInfoId)));
    }

    public Optional<Integer> findTurnByUser(User blackUser, User whiteUser) throws SQLException {
        ResultSet rs = findBoardResultSet(blackUser, whiteUser);

        if (!rs.next())
            return Optional.empty();

        return Optional.ofNullable(rs.getInt(4));
    }

    public void saveBoardByUserName(Board board, User blackUser, User whiteUser, int turn) throws SQLException {
        dbConnector.executeUpdate("UPDATE gameinfo SET turn = ? WHERE black = ? AND white = ?",
                String.valueOf(turn), blackUser.getName(), whiteUser.getName());

        ResultSet rs = findBoardResultSet(blackUser, whiteUser);

        if (!rs.next())
            return;

        int gameInfoId = rs.getInt(1);

        cellDAO.updateCellsByBoardId(board, gameInfoId);
    }

    public boolean deleteBoardByUser(User blackUser, User whiteUser) throws SQLException {
        ResultSet rs = findBoardResultSet(blackUser, whiteUser);

        if (!rs.next())
            return false;

        int gameInfoId = rs.getInt(1);

        cellDAO.deleteCellsByUser(gameInfoId);

        dbConnector.executeUpdate("DELETE FROM gameinfo WHERE black = ? AND white = ?", blackUser.getName(),
                whiteUser.getName());

        return !findBoardByUser(blackUser, whiteUser).isPresent();
    }

    private ResultSet findBoardResultSet(User blackUser, User whiteUser) throws SQLException {
        return dbConnector.executeQuery("SELECT * FROM gameinfo WHERE black = ? AND white = ?", blackUser.getName(),
                whiteUser.getName());
    }
}
