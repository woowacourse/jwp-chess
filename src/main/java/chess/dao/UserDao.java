package chess.dao;

import chess.dao.dto.UserDto;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<UserDto> userRowMapper = (resultSet, rowNum) -> UserDto.of(
        resultSet.getLong("id"),
        resultSet.getString("name"),
        resultSet.getString("password"),
        resultSet.getTimestamp("created_time").toLocalDateTime()
    );

    public UserDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long insert(final UserDto userDto) {
        final String sql = "INSERT INTO user(name, password) VALUES (?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final PreparedStatementCreator preparedStatementCreator = con -> {
            final PreparedStatement preparedStatement = con
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, userDto.getName());
            preparedStatement.setString(2, userDto.getPassword());
            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public UserDto selectByName(final String name) {
        final String sql = "SELECT * FROM user WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, name);
    }

    public UserDto selectById(final long id) {
        final String sql = "SELECT * FROM user WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, id);
    }

}
