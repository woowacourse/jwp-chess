package chess.dao;

import chess.domain.game.MoveRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommandDao {
    private final JdbcTemplate jdbcTemplate;

    public CommandDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MoveRequest> commandRowMapper = (resultSet, rowNum) -> new MoveRequest(
            resultSet.getString("move_from"),
            resultSet.getString("move_to")
    );

    public void insert(Long gameId, String move_from, String move_to) {
        String query = "INSERT INTO command (game_id, move_from, move_to) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, gameId, move_from, move_to);
    }

    public List<MoveRequest> findAllCommandOf(Long gameId) {
        String query = "SELECT move_from, move_to FROM command WHERE game_id = (?)";
        return jdbcTemplate.query(query, commandRowMapper, gameId);
    }
}
