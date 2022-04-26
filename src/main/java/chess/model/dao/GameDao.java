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
        String query = "insert into GAMES (password, turn) values (?, 'start')";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"game_id"});
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
        String query = "select turn from games where game_id = (?) limit 1";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, String.class, gameId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void update(Long gameId, String nextTurn) {
        String query = "UPDATE turns SET turn = (?) where game_id = (?)";
        jdbcTemplate.update(query, nextTurn, gameId);
    }

    public void deleteAll() {
        String query = "DELETE FROM turns";
        jdbcTemplate.update(query);
    }
}