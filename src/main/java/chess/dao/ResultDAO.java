package chess.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ResultDAO {
    private final JdbcTemplate jdbcTemplate;

    public ResultDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveGameResult(final String roomId, final int winnerId, final int loserId) {
        String query = "INSERT INTO result (game_id, winner, loser) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, roomId, winnerId, loserId);
    }

    public int winCountByUserId(final int id) {
        String query = "SELECT COUNT(*) FROM result WHERE winner = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, id);
    }

    public int loseCountByUserId(final int id) {
        String query = "SELECT COUNT(*) FROM result WHERE loser = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, id);
    }
}
