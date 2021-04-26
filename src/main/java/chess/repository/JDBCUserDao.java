package chess.repository;

import chess.dao.UserDao;
import chess.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JDBCUserDao implements UserDao {

    static RowMapper<User> userMapper = (rs, rowNum) -> new User(
            rs.getString("user_id"),
            rs.getString("name"),
            rs.getString("password"),
            rs.getTimestamp("created_date").toLocalDateTime()
    );

    private final JdbcTemplate jdbcTemplate;

    public JDBCUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update("insert into user value (?, ?, ?, ?)",
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getCreatedDate());
    }

    @Override
    public Optional<User> findByName(String name) {
        List<User> users = jdbcTemplate.query("select * from user where name = ?",
                userMapper,
                name);

        if (users.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(users.get(0));
    }
}
