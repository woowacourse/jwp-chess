package chess.repository.room;

import chess.util.JsonConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class SpringRoomDao {
    public static final String NO_SUCH_ROOM_NAME_ERROR = "존재하지 않는 방 이름입니다.";
    private JdbcTemplate jdbcTemplate;

    public SpringRoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addRoom(Room room) {
        String query = "INSERT INTO rooms (name, turn, state) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE turn = VALUES(turn), state = VALUES(state)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, room.getName());
            ps.setString(2, room.getTurn());
            ps.setString(3, room.getState().toString());
            return ps;
        });
    }

    public Room findByRoomName(String name) {
        String query = "SELECT * FROM rooms WHERE name = ?";
        return jdbcTemplate.queryForObject(
                query,
                (resultSet, rowNum) -> {
                    System.out.println(resultSet.getString("state"));
                    return new Room(
                            resultSet.getString("name"),
                            resultSet.getString("turn"),
                            JsonConverter.toJsonObject(resultSet.getString("state")));
                },
                name);
    }

    public void validateRoomExistence(String name) {
        String query = "SELECT COUNT(*) FROM rooms WHERE name = ?";
        int result = jdbcTemplate.queryForObject(query, Integer.class, name);
        if (result == 1) {
            throw new IllegalArgumentException(NO_SUCH_ROOM_NAME_ERROR);
        }
    }

    public List<String> getAllRoom() {
        String query = "SELECT name FROM rooms";
        return jdbcTemplate.query(query, (resultSet, rowNum) -> resultSet.getString("name"));
    }
}
