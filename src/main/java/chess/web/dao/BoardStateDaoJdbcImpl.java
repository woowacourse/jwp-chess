package chess.web.dao;

import chess.domain.state.StateType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class BoardStateDaoJdbcImpl implements BoardStateDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<StateType> boardStateRowMapper = (resultSet, rowNum) -> {
        return StateType.from(
                resultSet.getString("state")
        );
    };

    public BoardStateDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(StateType stateType) {
        final String sql = "insert into board (state) values (?)";
        jdbcTemplate.update(sql, stateType.getNotation());
    }

    @Override
    public void update(StateType stateType) {
        final String sql = "update board set state=?";
        jdbcTemplate.update(sql, stateType.getNotation());
    }

    @Override
    public StateType selectState() {
        final String sql = "select state from board order by id desc limit 1";
        return jdbcTemplate.queryForObject(sql, boardStateRowMapper);
    }

    @Override
    public void deleteAll() {
        final String sql = "delete from board";
        jdbcTemplate.update(sql);
    }
}
