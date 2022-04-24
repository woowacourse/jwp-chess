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

    // TODO: implement pagination
    public List<GameEntity> findAll() {
        final String sql = "SELECT id, name, running FROM game";

        List<GameEntity> games = jdbcTemplate.query(sql, new EmptySqlParameterSource(), rowMapper);
        return Collections.unmodifiableList(games);
    }

    public GameEntity findById(int gameId) {
        final String sql = "SELECT id, name, running FROM game WHERE id = :game_id";

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
        final String sql = "SELECT COUNT(*) FROM game WHERE id = :game_id";

        MapSqlParameterSource paramSource = new MapSqlParameterSource("game_id", gameId);
        int existingGameCount = jdbcTemplate.queryForObject(sql, paramSource, Integer.class);
        return existingGameCount > 0;
    }

    public int countAll() {
        final String sql = "SELECT COUNT(*) FROM game";
        return jdbcTemplate.queryForObject(sql, new EmptySqlParameterSource(), Integer.class);
    }

    public int countRunningGames() {
        final String sql = "SELECT COUNT(*) FROM game WHERE running = true";
        return jdbcTemplate.queryForObject(sql, new EmptySqlParameterSource(), Integer.class);
    }

    public int saveAndGetGeneratedId(EncryptedAuthCredentials authCredentials) {
        final String sql = "INSERT INTO game(name, password) VALUES (:name, :password)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource paramSource =  new BeanPropertySqlParameterSource(authCredentials);
        new Command(() -> jdbcTemplate.update(sql, paramSource, keyHolder))
                .executeOrThrow("게임을 생성하는데 실패하였습니다.");
        return keyHolder.getKey().intValue();
    }

    public void finishGame(int gameId) {
        final String sql = "UPDATE game SET running = false WHERE id = :game_id";

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("game_id", gameId);
        Command.execute(() -> jdbcTemplate.update(sql, paramSource))
                .throwOnNonEffected(gameId + "에 해당되는 게임을 종료시키는 데 실패하였습니다.");
    }

    public void deleteGame(EncryptedAuthCredentials authCredentials) {
        final String sql = "DELETE FROM game WHERE name = :name "
                + "AND password = :password AND running = false";

        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(authCredentials);
        Command.execute(() -> jdbcTemplate.update(sql, paramSource))
                .throwOnNonEffected("게임을 삭제하는 데 실패하였습니다!");
    }

    private final RowMapper<GameEntity> rowMapper = (resultSet, rowNum) ->
            new GameEntity(resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getBoolean("running"));
}
