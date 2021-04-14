package chess.daospring;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

public class RoomSpringDAO {
    private JdbcTemplate jdbcTemplate;

    public RoomSpringDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long createRoom(String roomName) {
        String query = "INSERT INTO room (roomName) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, roomName);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public Optional<Long> findRoomIdByName(String roomName) {
        try {
            String query = "SELECT roomId FROM room WHERE roomName = ? ORDER BY roomId DESC LIMIT 1";
            return jdbcTemplate.queryForObject(
                    query,
                    (resultSet, rowNum) -> {
                        return Optional.ofNullable(resultSet.getLong(1));
                    },
                    roomName);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
