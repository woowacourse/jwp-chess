package chess.repository.room;

import chess.util.JsonConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class SpringRoomDao {
    private JdbcTemplate jdbcTemplate;

    public SpringRoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long saveRoom(Room room) {
        String query = "INSERT INTO rooms (name, turn, state) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE turn = VALUES(turn), state = VALUES(state)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"room_id"});
            ps.setString(1, room.getName());
            ps.setString(2, room.getTurn());
            ps.setString(3, room.getState().toString());
            return ps;
        }, keyHolder);

        BigInteger generatedKey = (BigInteger) keyHolder.getKeyList().get(0).get("GENERATED_KEY");
        return generatedKey.longValue();
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

    public Room findById(long id) {
        String query = "SELECT * FROM rooms WHERE room_id = ?";
        try {
            return jdbcTemplate.queryForObject(
                    query,
                    (resultSet, rowNum) -> new Room(
                            resultSet.getString("name"),
                            resultSet.getString("turn"),
                            JsonConverter.fromJson(resultSet.getString("state"))),
                    id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoSuchRoomIdException();
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

    public void deleteRoom(String roomName) {
        try {
            String query = "DELETE FROM rooms WHERE name = ?";
            jdbcTemplate.update(query, roomName);
        } catch (Exception e) {
            throw new InvalidRoomDeleteException();
        }
    }

    public long getRoomIdByName(String roomName) {
        String query = "SELECT room_id FROM rooms WHERE name = ?";
        return (long) jdbcTemplate.queryForObject(query, Integer.class, roomName);
    }
}
