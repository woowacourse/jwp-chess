package chess.dao;

import chess.controller.dto.RoomDto;
import chess.exception.DataNotFoundException;
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
        String insertRoom = "INSERT INTO room (room_name) VALUES (?)";
        String findLastId = "SELECT last_insert_id();";

        jdbcTemplate.update(insertRoom, roomName);
        return jdbcTemplate.queryForObject(findLastId, Long.class);
    }

    public List<RoomDto> selectAll() {
        String query = "SELECT * FROM room";
        return jdbcTemplate.query(query, roomRowMapper);
    }


    public void delete(Long roomId) {
        String query = "DELETE FROM room WHERE room_id = (?)";
        if (jdbcTemplate.update(query, roomId) == 0) {
            throw new DataNotFoundException("해당 Id에 방 번호가 없습니다.");
        }
    }
}
