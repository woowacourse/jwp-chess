package chess.database.room;

import com.google.gson.JsonObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static chess.service.SpringChessService.gson;

@Repository
public class SpringRoomDAO {
    private JdbcTemplate jdbcTemplate;

    public SpringRoomDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addRoom(Room room) throws SQLException {
        String query = "INSERT INTO room (name, turn, state) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE turn = VALUES(turn), state = VALUES(state)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, room.getName());
            ps.setString(2, room.getTurn());
            ps.setString(3, room.getState().toString());
            return ps;
        });
    }

    public Room findByRoomName(String name) throws SQLException {
        String query = "SELECT * FROM room WHERE name = ?";
        return jdbcTemplate.queryForObject(
                query,
                (resultSet, rowNum) -> {
                    Room room = new Room(
                            resultSet.getString("name"),
                            resultSet.getString("turn"),
                            gson.fromJson(resultSet.getString("state"), JsonObject.class));
                    return room;
                },
                name);
    }

    public void validateRoomExistence(String name) throws SQLException {
        String query = "SELECT COUNT(*) FROM room WHERE name = ?";
        int result = jdbcTemplate.queryForObject(query, Integer.class, name);
        if (result == 1) {
            throw new IllegalArgumentException();
        }
    }

    public List<String> getAllRoom() throws SQLException {
        String query = "SELECT name FROM room";
        return jdbcTemplate.query(query, (resultSet, rowNum) -> resultSet.getString("name"));
    }
}
