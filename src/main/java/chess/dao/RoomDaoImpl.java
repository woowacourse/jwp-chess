package chess.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDaoImpl implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insert(final String name, final String password) {
        final String sql = "insert into room (name, password) values (?, ?)";
        return jdbcTemplate.update(sql, name, password);
    }
}
