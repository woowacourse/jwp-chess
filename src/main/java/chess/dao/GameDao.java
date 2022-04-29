package chess.dao;

import chess.controller.dto.response.GameIdentifiers;
import chess.domain.GameState;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertGame;

    public GameDao(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertGame = new SimpleJdbcInsert(dataSource)
                .withTableName("game")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(String name, String password, String salt, GameState state) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("password", password)
                .addValue("salt", salt)
                .addValue("state", state);
        return insertGame.executeAndReturnKey(parameters).longValue();
    }

    public List<GameIdentifiers> findAllGames() {
        String sql = "SELECT id, name FROM game";
        return namedParameterJdbcTemplate.query(sql,
                (resultSet, rowNum) -> new GameIdentifiers(resultSet.getLong("id"),
                        resultSet.getString("name")));
    }

    public Optional<String> findName(Long id) {
        String sql = "SELECT name FROM game WHERE id = :id";
        return find(sql, id);
    }

    public Optional<String> findPassword(Long id) {
        String sql = "SELECT password FROM game WHERE id = :id";
        return find(sql, id);
    }

    public Optional<String> findSalt(Long id) {
        String sql = "SELECT salt FROM game WHERE id = :id";
        return find(sql, id);
    }

    private Optional<String> find(String sql, Long id) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        try {
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(sql, namedParameters, String.class));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<GameState> findState(Long id) {
        String sql = "SELECT state FROM game WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        try {
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(sql, namedParameters, GameState.class));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void updateState(Long id, GameState gameState) {
        String sql = "UPDATE game SET state = :state WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("state", gameState.name())
                .addValue("id", id);
        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM game WHERE id = :id";
        MapSqlParameterSource parameter = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(sql, parameter);
    }
}
