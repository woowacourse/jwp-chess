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

    public void insertGame(int gameId, String turn) {
        String sql = "insert into game (game_id, current_turn) values (?,?)";
        jdbcTemplate.update(sql, gameId, turn);
    }

    public void deleteGame(int gameId) {
        String sql = "delete from game where game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }
}
