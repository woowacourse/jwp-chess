package chess.dao;

import chess.dto.RoomDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SpringChessRoomDao {
    private JdbcTemplate jdbcTemplate;

    public SpringChessRoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String createRoom(String roomName) {
        String query = "INSERT INTO chessroom(room_name) values(?)";
        this.jdbcTemplate.update(query, roomName);
        return createdRoomId();
    }

    private String createdRoomId() {
        String query = "SELECT MAX(room_id) from chessroom";
        return this.jdbcTemplate.queryForObject(query, String.class);
    }

    public List<RoomDto> findAllRoomIds() {
        String query = "SELECT room_id, room_name FROM chessroom";
        return jdbcTemplate.query(query,
                (rs, row) -> new RoomDto(
                        rs.getString("room_id"), rs.getString("room_name")));
    }

    public void deleteRoom(String roomNumber) {
        String query = "DELETE FROM chessroom WHERE room_id = ?";
        jdbcTemplate.update(query, roomNumber);
    }
}
