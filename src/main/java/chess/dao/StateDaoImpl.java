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

    public void insert(Long id, final State state) {
        final String sql = "insert into state (chess_id, name) values (?, ?)";
        final String stateName = StateToStringConverter.convert(state);
        jdbcTemplate.update(sql, id, stateName);
    }

    public State find(Long id, final Board board) {
        final String sql = "select name from state where chess_id = ?";
        return jdbcTemplate.queryForObject(
            sql,
            (resultSet, rowNum) -> {
                String name = resultSet.getString("name");
                return StringToStateConverter.convert(name, board);
            }, id);
    }

    public int delete(Long id) {
        final String sql = "delete from state where chess_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public void update(Long id, final State nowState, final State nextState) {
        final String sql = "update state set name = ? where chess_id = ? and name = ?";
        final String nowStateName = StateToStringConverter.convert(nowState);
        final String nextStateName = StateToStringConverter.convert(nextState);
        jdbcTemplate.update(sql, nextStateName, id, nowStateName);
    }
}
