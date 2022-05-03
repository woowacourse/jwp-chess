package chess.model.dao;

import chess.entity.GameEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class GameDao {
    private static final String INIT_TURN = "WHITE";
    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long initGame(String name, String password) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into game (name, password, turn) values (?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, INIT_TURN);
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<GameEntity> findAll() {
        String sql = "select id, name, password, turn from game";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    return new GameEntity(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("password"),
                            resultSet.getString("turn")
                    );
                });
    }

    public String findTurnById(Long id) {
        String query = "select turn from game where id = ?";
        return jdbcTemplate.queryForObject(query, String.class, id);
    }

    public String findPwdById(Long id) {
        String query = "select password from game where id = ?";
        return jdbcTemplate.queryForObject(query, String.class, id);
    }

    public void update(String nextTurn, Long id) {
        String query = "UPDATE game SET turn = (?) where id = ?";
        jdbcTemplate.update(query, nextTurn, id);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM game where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteAll() {
        String sql = "DELETE FROM game";
        jdbcTemplate.update(sql);
    }
}
