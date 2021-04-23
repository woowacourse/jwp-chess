package chess.repository;

import chess.domain.web.User;
import java.sql.PreparedStatement;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper = (resultSet, rowNum) ->
        new User(resultSet.getInt("id"), resultSet.getString("name"));

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addUser(User user) {
        String query = "INSERT INTO user(name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection
                .prepareStatement(query, new String[]{"id"});
            preparedStatement.setString(1, user.getName());
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public Optional<User> findUserById(int id) {
        String query = "SELECT id, name FROM user WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                query,
                userRowMapper,
                id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByName(String name) {
        String query = "SELECT id, name FROM user WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                query,
                userRowMapper,
                name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}