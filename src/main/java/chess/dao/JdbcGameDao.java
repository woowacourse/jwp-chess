package chess.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcGameDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertState(int roomId, String state) {
        final String sql = "insert into game(roomId, state) values(?, ?)";
        jdbcTemplate.update(sql, roomId, state);
    }

    @Override
    public void updateState(int roomId, String state) {
        final String sql = "update game set state = ? where roomId = ?";
        jdbcTemplate.update(sql, state, roomId);
    }

    @Override
    public String getState(int roomId) {
        final String sql = "select state from game where roomId = ?";
        return jdbcTemplate.queryForObject(sql, String.class, roomId);
    }
}
