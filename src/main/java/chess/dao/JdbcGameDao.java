package chess.dao;

import chess.entity.Game;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcGameDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long save(Game game) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final String sql = "insert into game (title, password, turn, status) values (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            preparedStatement.setString(1, game.getTitle());
            preparedStatement.setString(2, game.getPassword());
            preparedStatement.setString(3, game.getTurn());
            preparedStatement.setString(4, game.getStatus());
            return preparedStatement;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void remove(Game game) {
        final String sql = "delete from game where id = ? and password = ?";
        try {
            jdbcTemplate.update(sql, game.getId(), game.getPassword());
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("게임을 삭제할 수 없습니다.");
        }
    }

    public Game find(long id, String password) {
        final String sql = "select * from game where id = ? and password = ?";
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (resultSet, rowNum) ->
                            new Game(
                                    resultSet.getLong("id"),
                                    resultSet.getString("title"),
                                    resultSet.getString("turn"),
                                    resultSet.getString("status")
                            ),
                    id,
                    password
            );
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("게임(id=" + id + ")을 찾을 수 없습니다.");
        }
    }

    public List<Game> findAll() {
        final String sql = "select * from game";
        try {
            return jdbcTemplate.query(
                    sql,
                    (resultSet, rowNum) ->
                            new Game(
                                    resultSet.getLong("id"),
                                    resultSet.getString("title"),
                                    resultSet.getString("turn"),
                                    resultSet.getString("status")
                            )
            );
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("존재하는 게임이 없습니다.");
        }
    }
}