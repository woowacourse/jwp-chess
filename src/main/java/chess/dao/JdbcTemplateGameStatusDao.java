package chess.dao;

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
    public void init(int gameId) {
        jdbcTemplate.update("INSERT INTO game_status (status, game_id) values (?, ?)", "READY", gameId);
    }

    @Override
    public void update(String nowStatus, String nextStatus, int gameId) {
        String sql = "update game_status set status = ? where game_id = ? and status = ?";
        jdbcTemplate.update(sql, nextStatus, gameId, nowStatus);
    }

    @Override
    public String getStatus(int gameId) {
        final String sql = "select status from game_status where game_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }

    @Override
    public void reset(int gameId) {
        removeAll(gameId);
        String sql = "insert into game_status (status, game_id) values (?, ?)";
        jdbcTemplate.update(sql, GameStatus.READY.toString(), gameId);
    }

    private void removeAll(int gameId) {
        String sql = "delete from game_status where game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }
}
