package chess.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
