package chess.dao;

import chess.dao.converter.StateToStringConverter;
import chess.dao.converter.StringToStateConverter;
import chess.model.board.Board;
import chess.model.state.State;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StateDaoImpl implements StateDao {

    private final JdbcTemplate jdbcTemplate;

    public StateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insert(final String id, final State state) {
        final String sql = "insert into state (id, name) values (?, ?)";
        final String stateName = StateToStringConverter.convert(state);
        return jdbcTemplate.update(sql, id, stateName);
    }

    public State find(final String id, final Board board) {
        final String sql = "select name from state where id = ?";
        final String stateName = jdbcTemplate.queryForObject(sql, String.class, id);
        return StringToStateConverter.convert(stateName, board);
    }

    public int deleteFrom(final String id) {
        final String sql = "delete from state where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public void update(final String id, final State nowState, final State nextState) {
        final String sql = "update state set name = ? where name = ? and id = ?";
        final String nowStateName = StateToStringConverter.convert(nowState);
        final String nextStateName = StateToStringConverter.convert(nextState);
        jdbcTemplate.update(sql, nextStateName, nowStateName, id);
    }
}
