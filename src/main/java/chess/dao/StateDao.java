package chess.dao;

import chess.dao.converter.StateConverter;
import chess.model.state.State;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StateDao {

    private final JdbcTemplate jdbcTemplate;

    public StateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insert(final String id, final State state) {
        final String sql = "insert into state (id, name) values (?, ?)";
        final String stateName = StateConverter.convertToString(state);
        return jdbcTemplate.update(sql, id, stateName);
    }

    public String find(final String id) {
        final String sql = "select name from state where id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    public int deleteFrom(final String id) {
        final String sql = "delete from state where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public void update(final String id, final State nowState, final State nextState) {
        final String sql = "update state set name = ? where name = ? and id = ?";
        final String nowStateName = StateConverter.convertToString(nowState);
        final String nextStateName = StateConverter.convertToString(nextState);
        jdbcTemplate.update(sql, nextStateName, nowStateName, id);
    }
}
