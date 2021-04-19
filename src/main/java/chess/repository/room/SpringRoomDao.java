package chess.repository.room;

import chess.util.JsonConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class SpringRoomDao {
    private JdbcTemplate jdbcTemplate;

    public SpringRoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addRoom(Room room) {
        String query = "INSERT INTO rooms (name, turn, state) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE turn = VALUES(turn), state = VALUES(state)";
        jdbcTemplate.update(query, room.getName(), room.getTurn(), room.getState().toString());
    }

    public Room findByRoomName(String name) {
        String query = "SELECT * FROM rooms WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(
                    query,
                    (resultSet, rowNum) -> new Room(
                            resultSet.getString("name"),
                            resultSet.getString("turn"),
                            JsonConverter.fromJson(resultSet.getString("state"))),
                    name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoSuchRoomNameException();
        }
    }

    public void validateRoomExistence(String name) {
        String query = "SELECT COUNT(*) FROM rooms WHERE name = ?";
        int result = jdbcTemplate.queryForObject(query, Integer.class, name);
        if (1 <= result) {
            throw new DuplicateRoomNameException();
        }
    }

    public List<String> getAllRoom() {
        String query = "SELECT name FROM rooms";
        return jdbcTemplate.query(query, (resultSet, rowNum) -> resultSet.getString("name"));
    }

    public long deleteRoom(String roomName) {
        try {
            String query = "DELETE FROM rooms WHERE name = ?";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, new String[]{"room_id"});
                ps.setString(1, roomName);
                return ps;
            }, keyHolder);

            return Objects.requireNonNull(keyHolder.getKey()).longValue();
        } catch (Exception e) {
            throw new InvalidRoomDeleteException();
        }
    }
}
