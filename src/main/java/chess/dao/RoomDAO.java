package chess.dao;

import chess.dto.RoomDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDAO {
    private final JdbcTemplate jdbcTemplate;

    public RoomDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createRoom(final String title) {
        String query = "INSERT INTO room (title, status) VALUES (?, ?)";
        jdbcTemplate.update(query, title, "준비중");
    }

    public String createdRoomId() {
        String query = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(query, String.class);
    }

    public List<RoomDTO> allRooms() {
        String query = "SELECT id, title, status FROM room ORDER BY room.id DESC";
        return jdbcTemplate.query(query, mapper());
    }

    private RowMapper<RoomDTO> mapper() {
        return (resultSet, rowNum) -> {
            boolean playing = false;
            int status = resultSet.getInt("status");
            if (status == 1) {
                playing = true;
            }
            return new RoomDTO(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("status")
            );
        };
    }

    public void changeStatusEndByRoomId(final String roomId) {
        String query = "UPDATE room SET status = '종료됨' WHERE id = ?";
        jdbcTemplate.update(query, roomId);
    }

    public List<String> allRoomIds() {
        RowMapper<String> rowMapper = (resultSet, rowNum) -> resultSet.getString("id");

        String query = "SELECT id FROM room";
        return jdbcTemplate.query(query, rowMapper);
    }
}
