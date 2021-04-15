package chess.dao;

import chess.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (resultSet, rowNum) -> new User(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("password"),
            resultSet.getTimestamp("created_time").toLocalDateTime()
    );

    public long insert(final User user) {
        final String sql = "INSERT INTO user(name, password) VALUES (?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final PreparedStatementCreator preparedStatementCreator = con -> {
            final PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }


    public User selectByName(String name) {
        final String sql = "SELECT * FROM user WHERE name = ?";

        return jdbcTemplate.queryForObject(sql, userRowMapper, name);
    }

    public User selectById(long id) {
        final String sql = "SELECT * FROM user WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, userRowMapper, id);
    }
}
