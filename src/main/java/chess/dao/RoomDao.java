package chess.dao;

import chess.controller.dto.RoomDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDao {
    private JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<RoomDto> roomRowMapper = (resultSet, rowNum) -> new RoomDto(
            resultSet.getLong("room_id"),
            resultSet.getString("room_name")
    );

    public long insert(String roomName) {
        String query = "INSERT INTO room (room_name) VALUES (?)";
        String query2 = "SELECT last_insert_id();";

        jdbcTemplate.update(query, roomName);
        return jdbcTemplate.queryForObject(query2, Long.class);
    }

    public List<RoomDto> selectAll() {
        String query = "SELECT * FROM room";
        return jdbcTemplate.query(query, roomRowMapper);
    }


    public void delete(Long roomId) {
        String query = "DELETE FROM room WHERE room_id = (?)";
        jdbcTemplate.update(query, roomId);
    }
}
