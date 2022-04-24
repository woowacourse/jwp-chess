package chess.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.dao.converter.StateToStringConverter;
import chess.dao.converter.StringToStateConverter;
import chess.model.board.Board;
import chess.model.state.State;

@Repository
public class StateDaoImpl implements StateDao {

    private final JdbcTemplate jdbcTemplate;

    public StateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(final State state) {
        final String sql = "insert into state (name) values (?)";
        final String stateName = StateToStringConverter.convert(state);
        jdbcTemplate.update(sql, stateName);
    }

    public State find(final Board board) {
        final String sql = "select name from state";
        String stateName = jdbcTemplate.queryForObject(sql, String.class);
        return StringToStateConverter.convert(stateName, board);
    }

    public int delete() {
        final String sql = "delete from state";
        return jdbcTemplate.update(sql);
    }

    public void update(final State nowState, final State nextState) {
        final String sql = "update state set name = ? where name = ?";
        final String nowStateName = StateToStringConverter.convert(nowState);
        final String nextStateName = StateToStringConverter.convert(nextState);
        jdbcTemplate.update(sql, nextStateName, nowStateName);
    }
}
