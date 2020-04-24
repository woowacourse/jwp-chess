package wooteco.chess.dao;

import java.sql.SQLException;

import org.springframework.stereotype.Component;

import wooteco.chess.domain.piece.Team;
import wooteco.chess.exception.DataAccessException;

@Component
public class RoomDao {
    JdbcTemplate template = new JdbcTemplate();

    public void createRoom(String name) {
        try {
            PreparedStatementSetter pss = statement -> {
                statement.setString(1, name);
                statement.setString(2, Team.WHITE.getName());
            };
            final String sql = "INSERT INTO room(name, turn) VALUES (?, ?)";
            template.executeUpdate(sql, pss);
        } catch (SQLException e) {
            throw new DataAccessException();
        }
    }

    public int findRoomIdByName(String name) {
        try {
            RowMapper rm = rs -> Integer.parseInt(rs.getString("id"));
            PreparedStatementSetter pss = statement -> statement.setString(1, name);
            final String sql = "SELECT * FROM room WHERE name = ?";
            return (Integer)template.executeQueryWithPss(sql, pss, rm);
        } catch (SQLException e) {
            throw new DataAccessException();
        }
    }

    public void updateTurn(int roomId, Team turn) {
        try {
            PreparedStatementSetter pss = statement -> {
                statement.setString(1, turn.getName());
                statement.setInt(2, roomId);
            };
            final String sql = "UPDATE room SET turn = ? WHERE id = ?";
            template.executeUpdate(sql, pss);
        } catch (SQLException e) {
            throw new DataAccessException();
        }
    }

    public Team findTurnById(int roomId) {
        try {
            PreparedStatementSetter pss = statement -> statement.setInt(1, roomId);
            RowMapper rm = rs -> Team.of(rs.getString("turn"));
            final String sql = "SELECT turn FROM room WHERE id = ?";
            return (Team)template.executeQueryWithPss(sql, pss, rm);
        } catch (SQLException e) {
            throw new DataAccessException();
        }
    }
}
