package chess.dao;

import chess.controller.dto.MoveDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommandDao {
    private JdbcTemplate jdbcTemplate;

    public CommandDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MoveDto> commandRowMapper = (resultSet, rowNum) -> new MoveDto(
            resultSet.getLong("room_id"),
            resultSet.getString("move_from"),
            resultSet.getString("move_to")
    );

    public void insert(Long roomId, String move_from, String move_to) {
        String query = "INSERT INTO command (room_id, move_from, move_to) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, roomId, move_from, move_to);
    }

    public List<MoveDto> findAllCommandOf(Long roomId) {
        String query = "SELECT room_id, move_from, move_to FROM command WHERE room_id = (?)";
        return jdbcTemplate.query(query, commandRowMapper, roomId);
    }
}
