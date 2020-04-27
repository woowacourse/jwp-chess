package wooteco.chess.dao;

import java.sql.SQLException;
import java.util.Optional;

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

    public Optional<Integer> findRoomIdByName(String name) {
        try {
            RowMapper rm = rs -> rs.getInt("id");
            PreparedStatementSetter pss = statement -> statement.setString(1, name);
            final String sql = "SELECT * FROM room WHERE name = ?";
            Object result = template.executeQueryWithPss(sql, pss, rm);
            return Optional.ofNullable((Integer)result);
        } catch (SQLException e) {
            throw new DataAccessException();
        }
    }

    public Optional<String> findRoomNameById(int id) {
        try {
            RowMapper rm = rs -> rs.getString("name");
            PreparedStatementSetter pss = statement -> statement.setInt(1, id);
            final String sql = "SELECT * FROM room WHERE id = ?";
            return Optional.ofNullable((String)template.executeQueryWithPss(sql, pss, rm));
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
