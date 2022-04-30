package chess.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcGameDao implements GameDao {

    public static final String UPDATE_GAME_SET_STATE_SQL = "update game set state = ? where roomId = ?";
    private final JdbcTemplate jdbcTemplate;

    public JdbcGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertState(int roomId) {
        final String sql = "insert into game(roomId, state) values(?, 'run')";
        jdbcTemplate.update(sql, roomId);
    }

    @Override
    public void initializeState(int roomId) {
        final String sql = UPDATE_GAME_SET_STATE_SQL;
        jdbcTemplate.update(sql, "run", roomId);
    }

    @Override
    public void updateStateEnd(int roomId) {
        final String sql = UPDATE_GAME_SET_STATE_SQL;
        jdbcTemplate.update(sql, "end", roomId);
    }

    @Override
    public String getState(int roomId) {
        final String sql = "select state from game where roomId = ?";
        return jdbcTemplate.queryForObject(sql, String.class, roomId);
    }
}
