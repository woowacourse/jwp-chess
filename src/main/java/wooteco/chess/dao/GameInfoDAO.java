package wooteco.chess.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.stereotype.Component;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Status;
import wooteco.chess.domain.player.User;
import wooteco.chess.util.DBConnector;

@Component
public class GameInfoDAO {

    private BoardDAO boardDAO;
    private DBConnector dbConnector;

    public GameInfoDAO(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
        this.boardDAO = new BoardDAO(dbConnector);
    }

    public void addGameInfo(Board board, User blackUser, User whiteUser, int turn) throws SQLException {
        dbConnector.executeUpdate("INSERT INTO gameinfo (black, white, turn) VALUES (?, ?, ?)", blackUser.getName(),
                whiteUser.getName(), String.valueOf(turn));

        ResultSet rs = findGameInfoResultSet(blackUser, whiteUser);

        if (!rs.next()) {
            return;
        }

        int gameInfoId = rs.getInt("id");

        boardDAO.addBoard(board, gameInfoId);
    }

    public Optional<Board> findGameInfoByUser(User blackUser, User whiteUser) throws SQLException {
        ResultSet rs = findGameInfoResultSet(blackUser, whiteUser);

        if (!rs.next()) {
            return Optional.empty();
        }

        int gameInfoId = rs.getInt("id");

        return Optional.ofNullable(BoardFactory.of(boardDAO.findBoardByGameInfoId(gameInfoId)));
    }

    public Optional<Integer> findTurnByUser(User blackUser, User whiteUser) throws SQLException {
        ResultSet rs = findGameInfoResultSet(blackUser, whiteUser);

        if (!rs.next()) {
            return Optional.empty();
        }

        return Optional.ofNullable(rs.getInt("turn"));
    }

    public void saveGameInfoByUserName(Board board, User blackUser, User whiteUser, Status status) throws SQLException {
        dbConnector.executeUpdate("UPDATE gameinfo SET turn = ? WHERE black = ? AND white = ?",
                String.valueOf(status.getTurn()), blackUser.getName(), whiteUser.getName());

        ResultSet rs = findGameInfoResultSet(blackUser, whiteUser);

        if (!rs.next()) {
            return;
        }

        int gameInfoId = rs.getInt("id");

        boardDAO.updateBoardByGameInfoId(board, gameInfoId);
    }

    public void deleteGameInfoByUser(User blackUser, User whiteUser) throws SQLException {
        ResultSet rs = findGameInfoResultSet(blackUser, whiteUser);

        if (!rs.next()) {
            return;
        }
        int gameInfoId = rs.getInt("id");

        boardDAO.deleteBoardByGameInfoId(gameInfoId);

        dbConnector.executeUpdate("DELETE FROM gameinfo WHERE black = ? AND white = ?", blackUser.getName(),
                whiteUser.getName());
    }

    private ResultSet findGameInfoResultSet(User blackUser, User whiteUser) throws SQLException {
        return dbConnector.executeQuery("SELECT * FROM gameinfo WHERE black = ? AND white = ?", blackUser.getName(),
                whiteUser.getName());
    }
}
