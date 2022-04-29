package chess.dao;

import chess.domain.auth.EncryptedAuthCredentials;
import chess.entity.GameEntity;
import java.util.Collections;
import java.util.List;
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
public class GameDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public GameDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<GameEntity> findAll() {
        final String sql = "SELECT id, name, running FROM game";

        List<GameEntity> games = jdbcTemplate.query(sql, new EmptySqlParameterSource(), rowMapper);
        return Collections.unmodifiableList(games);
    }

    public GameEntity findById(int gameId) {
        final String sql = "SELECT id, name, running FROM game WHERE id = :game_id";

        MapSqlParameterSource paramSource = new MapSqlParameterSource("game_id", gameId);
        return new StatementExecutor<>(() -> jdbcTemplate.queryForObject(sql, paramSource, rowMapper))
                .executeOrThrow(() -> new IllegalArgumentException("존재하지 않는 게임입니다."));
    }

    public boolean checkById(int gameId) {
        final String sql = "SELECT COUNT(*) FROM game WHERE id = :game_id";

        MapSqlParameterSource paramSource = new MapSqlParameterSource("game_id", gameId);
        int existingGameCount = new StatementExecutor<>(
                () -> jdbcTemplate.queryForObject(sql, paramSource, Integer.class))
                .execute();
        return existingGameCount > 0;
    }

    public int countAll() {
        final String sql = "SELECT COUNT(*) FROM game";

        SqlParameterSource paramSource = new EmptySqlParameterSource();
        return new StatementExecutor<>(() -> jdbcTemplate.queryForObject(sql, paramSource, Integer.class))
                .execute();
    }

    public int countRunningGames() {
        final String sql = "SELECT COUNT(*) FROM game WHERE running = true";

        SqlParameterSource paramSource = new EmptySqlParameterSource();
        return new StatementExecutor<>(() -> jdbcTemplate.queryForObject(sql, paramSource, Integer.class))
                .execute();
    }

    public int saveAndGetGeneratedId(EncryptedAuthCredentials authCredentials) {
        final String sql = "INSERT INTO game(name, password) VALUES (:name, :password)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(authCredentials);

        new StatementExecutor<>(() -> jdbcTemplate.update(sql, paramSource, keyHolder))
                .executeOrThrow(() -> new IllegalArgumentException("게임을 생성하는데 실패하였습니다."));
        return keyHolder.getKey().intValue();
    }

    public void saveOpponent(EncryptedAuthCredentials authCredentials) {
        final String sql = "UPDATE game SET opponent_password = :password "
                + "WHERE name = :name AND NOT password = :password AND opponent_password IS NULL";

        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(authCredentials);
        new StatementExecutor<>(() -> jdbcTemplate.update(sql, paramSource))
               .updateAndThrowOnNonEffected(() -> new IllegalArgumentException("상대방 플레이어로 참여하는 데 실패하였습니다."));
    }

    public void finishGame(int gameId) {
        final String sql = "UPDATE game SET running = false WHERE id = :game_id";

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("game_id", gameId);

        new StatementExecutor<>(() -> jdbcTemplate.update(sql, paramSource))
                .updateAndThrowOnNonEffected(() -> new IllegalArgumentException("게임을 종료시키는 데 실패하였습니다."));
    }

    public void deleteGame(EncryptedAuthCredentials authCredentials) {
        final String sql = "DELETE FROM game WHERE name = :name "
                + "AND password = :password AND running = false";

        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(authCredentials);

        new StatementExecutor<>(() -> jdbcTemplate.update(sql, paramSource))
                .updateAndThrowOnNonEffected(() -> new IllegalArgumentException("게임을 삭제하는 데 실패하였습니다!"));
    }

    private final RowMapper<GameEntity> rowMapper = (resultSet, rowNum) ->
            new GameEntity(resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getBoolean("running"));
}
