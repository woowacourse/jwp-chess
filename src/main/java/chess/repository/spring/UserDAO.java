package chess.repository.spring;

import chess.domain.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {
    private static final RowMapper<User> ROW_MAPPER = (resultSet, rowNumber) -> {
        int id = resultSet.getInt("id");
        String password = resultSet.getString("password");
        String teamType = resultSet.getString("team_type");
        int roomId = resultSet.getInt("room_id");
        return new User(id, password, teamType, roomId);
    };

    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAllByRoomId(int roomId) {
        String query = "SELECT * FROM USER WHERE ROOM_ID = ?";
        return jdbcTemplate.query(query, ROW_MAPPER, roomId);
    }

    public void insertUser(String password, String teamType, int roomId) {
        String query = "INSERT INTO USER (PASSWORD, TEAM_TYPE, ROOM_ID) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, password, teamType, roomId);
    }

    public void deleteAllByRoomId(int roomId) {
        String query = "DELETE FROM USER WHERE ROOM_ID = ?";
        jdbcTemplate.update(query, roomId);
    }

    public void deleteUser(User user) {
        String query = "DELETE FROM USER WHERE ID = ?";
        jdbcTemplate.update(query, user.getId());
    }
}
