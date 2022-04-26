package chess.dao;

import chess.dao.GameStatusDao;
import chess.domain.GameStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateGameStatusDao implements GameStatusDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateGameStatusDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void init() {
        jdbcTemplate.update("INSERT INTO game_status (status) values (?)", "READY");
    }

    @Override
    public void update(String nowStatus, String nextStatus) {
        String sql = "update game_status set status = ? where status = ?";
        jdbcTemplate.update(sql, nextStatus, nowStatus);
    }

    @Override
    public String getStatus() {
        final String sql = "select * from game_status";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    @Override
    public void reset() {
        removeAll();
        String sql = "insert into game_status (status) values (?)";
        jdbcTemplate.update(sql, GameStatus.READY.toString());
    }

    private void removeAll() {
        String sql = "truncate table game_status";
        jdbcTemplate.update(sql);
    }
}
