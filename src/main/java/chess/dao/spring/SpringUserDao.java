package chess.dao.spring;

import chess.dao.UserDao;
import chess.domain.board.Team;
import chess.dto.web.UsersInRoomDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SpringUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public SpringUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(String userName) {
        String query = "INSERT INTO users (name) "
            + "SELECT * FROM (SELECT ?) AS tmp "
            + "WHERE NOT EXISTS (SELECT * FROM users WHERE name = ?)";
        jdbcTemplate.update(query, userName, userName);
    }

    public UsersInRoomDto usersInRoom(String roomId) {
        String query = "SELECT"
            + " white_user.name AS whiteName,"
            + " white_user.win AS whiteWin,"
            + " white_user.lose AS whiteLose,"
            + " black_user.name AS blackName,"
            + " black_user.win AS blackWin,"
            + " black_user.lose AS blackLose "
            + "FROM"
            + " rooms AS r"
            + " INNER JOIN users AS black_user ON r.black = black_user.name"
            + " INNER JOIN users AS white_user ON r.white = white_user.name "
            + "WHERE r.id = ?";

        return jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> new UsersInRoomDto(
            resultSet.getString("whiteName"),
            resultSet.getString("whiteWin"),
            resultSet.getString("whiteLose"),
            resultSet.getString("blackName"),
            resultSet.getString("blackWin"),
            resultSet.getString("blackLose")
        ), roomId);
    }

    public void updateStatistics(String roomId, Team winnerTeam) {
        String winner = "white";
        String loser = "black";
        if (winnerTeam.isBlack()) {
            winner = "black";
            loser = "white";
        }
        updateWinner(roomId, winner);
        updateLoser(roomId, loser);
    }

    private void updateWinner(String roomId, String winner) {
        String updateWinnerQuery = "UPDATE users "
            + "SET users.win = users.win + 1 "
            + "WHERE users.name = (SELECT " + winner + " FROM rooms WHERE id = ?)";

        jdbcTemplate.update(updateWinnerQuery, roomId);
    }

    private void updateLoser(String roomId, String loser) {
        String updateLoserQuery = "UPDATE users "
            + "SET users.lose = users.lose + 1 "
            + "WHERE users.name = (SELECT " + loser + " FROM rooms WHERE id = ?);";

        jdbcTemplate.update(updateLoserQuery, roomId);
    }
}
