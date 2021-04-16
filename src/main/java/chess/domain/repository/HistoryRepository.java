package chess.domain.repository;

import chess.domain.dto.HistoryDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HistoryRepository {
    private final JdbcTemplate jdbcTemplate;

    public HistoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<HistoryDto> rowMapper = (resultSet, rowNum) ->
            new HistoryDto(resultSet.getString("history_id"), resultSet.getString("name"));
    ;

    public void insert(String name) {
        final String query = "INSERT INTO History (name) VALUES (?)";
        jdbcTemplate.update(query, name);
    }

    public Optional<Integer> findIdByName(String name) {
        final String query = "SELECT history_id FROM History WHERE name = ? AND is_end = false " +
                "ORDER BY history_id DESC limit 1";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, Integer.class, name));
    }

    public int delete(String name) {
        final String query = "DELETE FROM History WHERE Name = ?";
        return jdbcTemplate.update(query, name);
    }

    public List<HistoryDto> selectActive() {
        final String query = "SELECT * FROM History WHERE is_end = false";
        return jdbcTemplate.query(query, rowMapper);
    }

    public void updateEndState(String id) {
        final String query = "UPDATE History SET is_end = 1 WHERE history_id = ?";
        jdbcTemplate.update(query, id);
    }
}
