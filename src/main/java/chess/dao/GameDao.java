package chess.dao;

import java.sql.PreparedStatement;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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

    public int insertWithKeyHolder(String turn) {
        String sql = "insert into game (current_turn) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"game_id"});
            ps.setString(1, turn);
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }
}
