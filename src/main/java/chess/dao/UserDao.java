package chess.dao;

import chess.entity.UserEntity;
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

    private final RowMapper<UserEntity> userRowMapper = (resultSet, rowNum) -> new UserEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("password"),
            resultSet.getTimestamp("created_time").toLocalDateTime()
    );

    public long insert(final UserEntity userEntity) {
        final String sql = "INSERT INTO user(name, password) VALUES (?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final PreparedStatementCreator preparedStatementCreator = con -> {
            final PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, userEntity.getName());
            preparedStatement.setString(2, userEntity.getPassword());
            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }


    public UserEntity selectByName(String name) {
        final String sql = "SELECT * FROM user WHERE name = ?";

        return jdbcTemplate.queryForObject(sql, userRowMapper, name);
    }
}
