package chess.model.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class GameDao {
    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long saveGame(String password) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into GAMES (password, turn) values (?, 'white')";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"game_id"});
            preparedStatement.setString(1, password);
            return  preparedStatement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public List<Long> findAllGameId() {
        String sql = "select game_id from games";
        return jdbcTemplate.queryForList(sql, Long.class);
    }

    public Optional<String> findTurnByGameId(Long gameId) {
        String sql = "select turn from games where game_id = (?) limit 1";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, String.class, gameId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void updateTurnByGameId(Long gameId, String nextTurn) {
        String sql = "update games set turn = (?) where game_id = (?)";
        jdbcTemplate.update(sql, nextTurn, gameId);
    }

    public void deleteByGameId(Long gameId) {
        String sql = "delete from games where game_id = (?)";
        jdbcTemplate.update(sql, gameId);
    }

    public String findPasswordByGameId(Long gameId) {
        String sql = "select password from games where game_id = (?)";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }
}