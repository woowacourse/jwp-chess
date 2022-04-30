package chess.repository;

import chess.domain.state.State;
import chess.dto.GameDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(String title, String password, String state) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("game")
                .usingGeneratedKeyColumns("id");
        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("password", password);
        params.put("state", state);

        int id = simpleJdbcInsert.executeAndReturnKey(params).intValue();
        return id;
    }

    public List<GameDto> findAll() {
        String sql = "select id, title, state from game";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            GameDto gameDto = new GameDto(rs.getInt("id"), rs.getString("title"), rs.getString("state"));
            return gameDto;
        });
    }

    public State findState(int id) {
        String sql = "select state from game where id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            State state = State.of(rs.getString("state"));
            return state;
        }, id);
    }

    public String findPassword(int id) {
        String sql = "select password from game where id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    public Long findGameCount() {
        String sql = "select count(*) from game";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public int update(String state, int id) {
        String sql = "update game set state=? where id=?";
        jdbcTemplate.update(sql, state, id);
        return id;
    }

    public int delete(int id) {
        String sql = "delete from game where id = ?";
        return jdbcTemplate.update(sql, id);
    }
}