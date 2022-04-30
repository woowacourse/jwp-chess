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

    public void insertGame(int gameId, String turn, String status) {
        String sql = "insert into game (game_id, current_turn, status) values (?,?,?)";
        jdbcTemplate.update(sql, gameId, turn, status);
    }

    public void updateStatus(int gameId, String status) {
        String sql = "update game set status=? where game_id=?";
        jdbcTemplate.update(sql, status, gameId);
    }

    public void deleteGame(int gameId) {
        String sql = "delete from game where game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }
}
