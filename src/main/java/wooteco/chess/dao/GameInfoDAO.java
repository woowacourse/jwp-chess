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

    public void addGameInfo(Board board, User user, int turn) throws SQLException {
        String blackUserId = findUsersId(user);

        dbConnector.executeUpdate("INSERT INTO gameinfo (user, turn) VALUES (?, ?)",
            blackUserId, String.valueOf(turn));

        ResultSet rs = findGameInfoResultSet(blackUserId);

        if (!rs.next()) {
            return;
        }

        int gameInfoId = rs.getInt("id");

        boardDAO.addBoard(board, gameInfoId);
    }

    public Optional<Board> findGameInfoByUser(User user) throws SQLException {
        ResultSet rs = findGameInfoResultSet(findUsersId(user));

        if (!rs.next()) {
            return Optional.empty();
        }

        int gameInfoId = rs.getInt("id");

        return Optional.ofNullable(BoardFactory.of(boardDAO.findBoardByGameInfoId(gameInfoId)));
    }

    public Optional<Integer> findTurnByUser(User user) throws SQLException {
        ResultSet rs = findGameInfoResultSet(findUsersId(user));

        if (!rs.next()) {
            return Optional.empty();
        }

        return Optional.ofNullable(rs.getInt("turn"));
    }

    public void saveGameInfoByUserName(Board board, User user, Status status) throws SQLException {
        dbConnector.executeUpdate("UPDATE gameinfo SET turn = ? WHERE user = ?",
                String.valueOf(status.getTurn()), user.getName());

        ResultSet rs = findGameInfoResultSet(findUsersId(user));

        if (!rs.next()) {
            return;
        }

        int gameInfoId = rs.getInt("id");

        boardDAO.updateBoardByGameInfoId(board, gameInfoId);
    }

    public void deleteGameInfoByUser(User user) throws SQLException {
        ResultSet rs = findGameInfoResultSet(findUsersId(user));

        if (!rs.next()) {
            return;
        }
        int gameInfoId = rs.getInt("id");

        boardDAO.deleteBoardByGameInfoId(gameInfoId);

        dbConnector.executeUpdate("DELETE FROM gameinfo WHERE user = ?",
            user.getName());
    }

    private ResultSet findGameInfoResultSet(String userId) throws SQLException {
        return dbConnector.executeQuery("SELECT * FROM gameinfo WHERE user = ?", userId);
    }

    private String findUsersId(User user) throws SQLException {
        ResultSet rs = dbConnector.executeQuery("SELECT id FROM user where name = ?", user.getName());

        if (!rs.next()) {
            return "";
        }
        return rs.getString("id");
    }
}
