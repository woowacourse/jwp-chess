package chess.daospring;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class RoomDAO {
    private JdbcTemplate jdbcTemplate;

    public RoomDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long createRoom(String roomName) throws SQLException {
        String query = "INSERT INTO room (roomName) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, roomName);
            return ps;
        }, keyHolder);
        return (long) keyHolder.getKey();
    }

    public Optional<Long> findRoomIdByName(String roomName) throws SQLException {
        String query = "SELECT roomId FROM room WHERE roomName = ? ORDER BY roomId DESC";
        return jdbcTemplate.queryForObject(query,
                Optional.class, roomName);
    }
}
