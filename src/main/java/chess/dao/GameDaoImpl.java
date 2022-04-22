package chess.dao;

import chess.domain.GameState;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class GameDaoImpl implements GameDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertGame;

    public GameDaoImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertGame = new SimpleJdbcInsert(dataSource)
                .withTableName("game");
    }

    @Override
    public void save(long id) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("game_id", id);
        parameters.put("state", GameState.READY);
        insertGame.execute(parameters);
    }

    @Override
    public Optional<GameState> load(long id) {
        String sql = "SELECT state FROM game WHERE game_id = :game_id";

        SqlParameterSource namedParameters = new MapSqlParameterSource("game_id", id);

        try {
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(sql, namedParameters, GameState.class));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void updateState(long id, GameState gameState) {
        String query = "UPDATE game SET state = :game_state WHERE game_id = :game_id";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("game_state", gameState.name());
        parameters.put("game_id", id);
        namedParameterJdbcTemplate.update(query, parameters);
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM game WHERE game_id = :game_id";
        MapSqlParameterSource parameter = new MapSqlParameterSource("game_id", id);
        namedParameterJdbcTemplate.update(query, parameter);
    }
}
