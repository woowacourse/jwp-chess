package chess.dao;

import chess.domain.event.Event;
import chess.entity.EventEntity;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class EventDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public EventDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Event> findAllByGameId(int gameId) {
        final String sql = "SELECT type, description FROM event WHERE game_id = :game_id";
        SqlParameterSource paramSource = new MapSqlParameterSource("game_id", gameId);
        return jdbcTemplate.query(sql, paramSource, eventRowMapper);
    }

    public void save(int gameId, Event event) {
        final String sql = "INSERT INTO event (game_id, type, description)"
                + "VALUES (:gameId, :type, :description)";

        EventEntity eventEntity = event.toEntityOf(gameId);
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(eventEntity);
        jdbcTemplate.update(sql, paramSource);
    }

    public void deleteAllByGameId(int gameId) {
        final String sql = "DELETE FROM event WHERE game_id = :game_id";
        SqlParameterSource paramSource = new MapSqlParameterSource("game_id", gameId);
        int deletedRowCount = jdbcTemplate.update(sql, paramSource);
        checkDeleteResult(deletedRowCount);
    }

    private void checkDeleteResult(int deletedRowCount) {
        if (deletedRowCount == 0) {
            throw new IllegalArgumentException("해당되는 이벤트가 없습니다!");
        }
    }

    private final RowMapper<Event> eventRowMapper = (resultSet, rowNum) ->
            Event.of(resultSet.getString("type"),
                    resultSet.getString("description"));
}
