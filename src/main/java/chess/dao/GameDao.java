package chess.dao;

import chess.dao.entity.GameEntity;
import chess.domain.GameState;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertGame;

    private final RowMapper<GameEntity> gameEntityRowMapper = (resultSet, rowNum) -> new GameEntity(
            resultSet.getLong("id"), resultSet.getString("name"),
            resultSet.getString("password"), resultSet.getString("salt"),
            GameState.valueOf(resultSet.getString("state")));

    public GameDao(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertGame = new SimpleJdbcInsert(dataSource)
                .withTableName("game")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(GameEntity gameEntity) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(gameEntity);
        try {
            return insertGame.executeAndReturnKey(parameters).longValue();
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("이미 존재하는 게임입니다.");
        }
    }

    public List<GameEntity> findAll() {
        String sql = "SELECT * FROM game";
        return namedParameterJdbcTemplate.query(sql, gameEntityRowMapper);
    }

    public GameEntity findById(Long id) {
        String sql = "SELECT * FROM game WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, gameEntityRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("존재하지 않는 게임입니다.", e.getExpectedSize());
        }
    }

    public void updateState(Long id, GameState state) {
        String sql = "UPDATE game SET state = :state WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("state", state.name())
                .addValue("id", id);
        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM game WHERE id = :id";
        MapSqlParameterSource parameter = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(sql, parameter);
    }
}
