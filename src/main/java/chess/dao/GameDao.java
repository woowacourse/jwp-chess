package chess.dao;

import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private static final String TABLE_NAME = "game";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GameDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public int saveAndGetGeneratedId() {
        final String sql = addTable("INSERT INTO %s (state) VALUES (:state)");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource paramSource = new MapSqlParameterSource("state", GameState.RUNNING.name());
        namedParameterJdbcTemplate.update(sql, paramSource, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void finishGame(int gameId) {
        final String sql = addTable("UPDATE %s SET state = :state WHERE id = :game_id");
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("game_id", gameId);
        paramSource.addValue("state", GameState.OVER.name());

        namedParameterJdbcTemplate.update(sql, paramSource);
    }

    public boolean checkById(int gameId) {
        final String sql = addTable("SELECT COUNT(*) FROM %s WHERE id = :game_id");

        MapSqlParameterSource paramSource = new MapSqlParameterSource("game_id", gameId);
        int existingGameCount = namedParameterJdbcTemplate.queryForObject(sql, paramSource, Integer.class);
        return existingGameCount > 0;
    }

    public int countAll() {
        final String sql = addTable("SELECT COUNT(*) FROM %s");
        return namedParameterJdbcTemplate.queryForObject(sql, new EmptySqlParameterSource(), Integer.class);
    }

    public int countByState(GameState state) {
        final String sql = addTable("SELECT COUNT(*) FROM %s WHERE state = :state");

        MapSqlParameterSource paramSource = new MapSqlParameterSource("state", state.name());
        return namedParameterJdbcTemplate.queryForObject(sql, paramSource, Integer.class);
    }

    protected String addTable(String sql) {
        return String.format(sql, TABLE_NAME);
    }
}
