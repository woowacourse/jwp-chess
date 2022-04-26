package chess.repository;

import chess.domain.state.State;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String state) {
        String sql = "insert into game(state) values (?)";
        jdbcTemplate.update(sql, state);
    }

    public State findState() {
        String sql = "select state from game";
        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> {
                    State state = State.of(rs.getString("state"));
                    return state;
                });
    }

    public Long findId() {
        String sql = "select id from game order by id DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public int update(String state, Long gameId) {
        String sql = "update game set state=? where id=?";
        return jdbcTemplate.update(sql, state, gameId);
    }

    public int delete() {
        String sql = "delete from game";
        return jdbcTemplate.update(sql);
    }

    public Long findGameCount() {
        String sql = "select count(*) from game";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
