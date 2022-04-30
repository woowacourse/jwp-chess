package chess.dao;

import chess.entity.EventEntity;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class EventDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public EventDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<EventEntity> eventRowMapper = (resultSet, rowNum) ->
            new EventEntity(resultSet.getString("type"),
                    resultSet.getString("description"));

    public List<EventEntity> findAllByGameId(int gameId) {
        final String sql = "SELECT type, description FROM event WHERE game_id = :game_id";
        SqlParameterSource paramSource = new MapSqlParameterSource("game_id", gameId);
        return namedParameterJdbcTemplate.query(sql, paramSource, eventRowMapper);
    }

    public void save(int gameId, EventEntity event) {
        final String sql = "INSERT INTO event (game_id, type, description)"
                + "VALUES (:game_id, :type, :description)";

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("game_id", gameId);
        paramSource.addValue("type", event.getType());
        paramSource.addValue("description", event.getDescription());

        namedParameterJdbcTemplate.update(sql, paramSource);
    }
}
