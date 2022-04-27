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
    public void startGame(int roomId) {
        if (isExist(roomId)) {
            updateGame(roomId);
            return;
        }
        final String sql = "insert into game(roomId, state) values(?, 'run')";
        jdbcTemplate.update(sql, roomId);
    }


    private boolean isExist(int roomId) {
        final String sql = "select count(*) from game where roomId = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, roomId) == 1;
    }

    private void updateGame(int roomId) {
        final String sql = "update game set state = 'run' where roomId = ?";
        jdbcTemplate.update(sql, roomId);
    }

    @Override
    public void endGame(int roomId) {
        final String sql = "update game set state = 'end' where roomId = ?";
        jdbcTemplate.update(sql, roomId);
    }

    @Override
    public String getState(int roomId) {
        final String sql = "select state from game where roomId = ?";
        return jdbcTemplate.queryForObject(sql, String.class, roomId);
    }
}
