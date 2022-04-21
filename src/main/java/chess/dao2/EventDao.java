package chess.dao2;

import chess.domain.event.Event;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class EventDao {

    private static final String TABLE_NAME = "event";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public EventDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<Event> eventRowMapper = (resultSet, rowNum) ->
            Event.of(resultSet.getString("type"),
                    resultSet.getString("description"));

    public List<Event> findAllByGameId(int gameId) {
        final String sql = addTable("SELECT type, description FROM %s WHERE game_id = :game_id");
        SqlParameterSource paramSource = new MapSqlParameterSource("game_id", gameId);
        return namedParameterJdbcTemplate.query(sql, paramSource, eventRowMapper);
    }

    public void save(int gameId, Event event) {
        final String sql = addTable("INSERT INTO %s (game_id, type, description) "
                + "VALUES (:game_id, :type, :description)");

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("game_id", gameId);
        paramSource.addValue("type", event.getType());
        paramSource.addValue("description", event.getDescription());

        namedParameterJdbcTemplate.update(sql, paramSource);
    }

    protected String addTable(String sql) {
        return String.format(sql, TABLE_NAME);
    }
}
