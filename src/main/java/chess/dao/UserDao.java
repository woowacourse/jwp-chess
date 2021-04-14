package chess.dao;

import chess.entity.UserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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

    public void insert(final UserEntity userEntity) {
         final String sql = "INSERT INTO user(name, password) VALUES (?, ?)";

         jdbcTemplate.update(sql, userEntity.getName(), userEntity.getPassword());
    }

    public void selectByName(String name) {
        final String sql = "SELECT * FROM user WHERE name = ?";

        jdbcTemplate.queryForObject(sql, userRowMapper, name);
    }
}
