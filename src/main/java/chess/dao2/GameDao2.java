package chess.dao2;

import chess.domain.auth.EncryptedAuthCredentials;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao2 {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GameDao2(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public int saveAndGetGeneratedId(EncryptedAuthCredentials authCredentials) {
        final String sql = "INSERT INTO game2(name, password) VALUES (:name, :password)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(authCredentials);
        namedParameterJdbcTemplate.update(sql, paramSource, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void finishGame(int gameId) {
        final String sql = "UPDATE game2 SET running = false WHERE id = :game_id";
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("game_id", gameId);

        namedParameterJdbcTemplate.update(sql, paramSource);
    }

    public boolean checkById(int gameId) {
        final String sql = "SELECT COUNT(*) FROM game2 WHERE id = :game_id";

        MapSqlParameterSource paramSource = new MapSqlParameterSource("game_id", gameId);
        int existingGameCount = namedParameterJdbcTemplate.queryForObject(sql, paramSource, Integer.class);
        return existingGameCount > 0;
    }

    public int countAll() {
        final String sql = "SELECT COUNT(*) FROM game2";
        return namedParameterJdbcTemplate.queryForObject(sql, new EmptySqlParameterSource(), Integer.class);
    }

    public int countRunningGames() {
        final String sql = "SELECT COUNT(*) FROM game2 WHERE running = true";
        return namedParameterJdbcTemplate.queryForObject(sql, new EmptySqlParameterSource(), Integer.class);
    }
}
