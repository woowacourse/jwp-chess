package chess.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {
    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateTurn(int gameId, String turn) {
        String sql = "UPDATE game SET current_turn=? WHERE game_id=?";
        jdbcTemplate.update(sql, turn, gameId);
    }

    public String findTurnById(int gameId) {
        String sql = "select current_turn from game where game_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }
}
