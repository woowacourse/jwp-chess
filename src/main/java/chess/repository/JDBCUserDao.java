package chess.repository;

import chess.dao.UserDao;
import chess.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCUserDao implements UserDao {

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
}
