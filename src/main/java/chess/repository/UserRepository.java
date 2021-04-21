package chess.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(String roomId, String password, String team) {
        final String query = "INSERT INTO User (room_id, password, team) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, roomId, password, team);
    }
}
