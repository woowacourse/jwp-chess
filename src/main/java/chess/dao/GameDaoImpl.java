package chess.dao;

import chess.domain.state.State;
import chess.entity.Game;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class GameDaoImpl implements GameDao {

    private JdbcTemplate jdbcTemplate;

    private RowMapper<Game> gameRowMapper = (rs, rowNum) ->
            new Game(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("password"),
                    rs.getString("state")
            );

    private RowMapper<State> stateRowMapper = (rs, rowNum) ->
            State.of(rs.getString("state"));


    public GameDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(Game game) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("game")
                .usingGeneratedKeyColumns("id");
        Map<String, String> params = new HashMap<>();
        params.put("title", game.getTitle());
        params.put("password", game.getPassword());
        params.put("state", game.getState());

        return simpleJdbcInsert.executeAndReturnKey(params).intValue();
    }

    @Override
    public List<Game> findAll() {
        String sql = "select * from game";
        return jdbcTemplate.query(sql, gameRowMapper);
    }

    @Override
    public Game findById(int id) {
        String sql = "select * from game where id = ?";
        return jdbcTemplate.queryForObject(sql, gameRowMapper, id);
    }

    @Override
    public State findState(int id) {
        String sql = "select state from game where id = ?";
        return jdbcTemplate.queryForObject(sql, stateRowMapper, id);
    }

    @Override
    public int update(Game game) {
        String sql = "update game set state=? where id = ?";
        jdbcTemplate.update(sql, game.getState(), game.getId());
        return game.getId();
    }

    @Override
    public int delete(int id) {
        String sql = "delete from game where id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
