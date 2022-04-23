package chess.dao2;

import chess.domain.auth.EncryptedAuthCredentials;
import chess.entity.GameEntity;
import java.util.Collections;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
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

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public GameDao2(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = namedParameterJdbcTemplate;
    }

    // TODO: implement pagination
    public List<GameEntity> findAll() {
        final String sql = "SELECT id, name, running FROM game2";

        List<GameEntity> games = jdbcTemplate.query(sql, new EmptySqlParameterSource(), rowMapper);
        return Collections.unmodifiableList(games);
    }

    public GameEntity findById(int gameId) {
        final String sql = "SELECT id, name, running FROM game2 WHERE id = :game_id";

        MapSqlParameterSource paramSource = new MapSqlParameterSource("game_id", gameId);
        return getFoundGame(jdbcTemplate.query(sql, paramSource, rowMapper));
    }

    private GameEntity getFoundGame(List<GameEntity> foundGame) {
        if (foundGame.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 게임입니다.");
        }
        return foundGame.get(0);
    }

    public boolean checkById(int gameId) {
        final String sql = "SELECT COUNT(*) FROM game2 WHERE id = :game_id";

        MapSqlParameterSource paramSource = new MapSqlParameterSource("game_id", gameId);
        int existingGameCount = jdbcTemplate.queryForObject(sql, paramSource, Integer.class);
        return existingGameCount > 0;
    }

    public int countAll() {
        final String sql = "SELECT COUNT(*) FROM game2";
        return jdbcTemplate.queryForObject(sql, new EmptySqlParameterSource(), Integer.class);
    }

    public int countRunningGames() {
        final String sql = "SELECT COUNT(*) FROM game2 WHERE running = true";
        return jdbcTemplate.queryForObject(sql, new EmptySqlParameterSource(), Integer.class);
    }

    public int saveAndGetGeneratedId(EncryptedAuthCredentials authCredentials) {
        final String sql = "INSERT INTO game2(name, password) VALUES (:name, :password)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(authCredentials), keyHolder);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("게임을 생성하는데 실패하였습니다.");
        }
        return keyHolder.getKey().intValue();
    }

    public void finishGame(int gameId) {
        final String sql = "UPDATE game2 SET running = false WHERE id = :game_id";

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("game_id", gameId);
        jdbcTemplate.update(sql, paramSource);
    }

    public void deleteGame(EncryptedAuthCredentials authCredentials) {
        final String sql = "DELETE FROM game2 WHERE name = :name "
                + "AND password = :password AND running = false";

        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(authCredentials);
        int deletedRowCount = jdbcTemplate.update(sql, paramSource);
        checkDeleteResult(deletedRowCount);
    }

    private void checkDeleteResult(int deletedRowCount) {
        if (deletedRowCount == 0) {
            throw new IllegalArgumentException("게임을 삭제하는 데 실패하였습니다!");
        }
    }

    final RowMapper<GameEntity> rowMapper = (resultSet, rowNum) ->
            new GameEntity(resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getBoolean("running"));
}
