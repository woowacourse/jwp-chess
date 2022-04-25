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
    public void saveNewRoom(final String roomName, final String password) {
        final String sql = "insert into room (name, password, gameState, turn) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, roomName, password, "playing", "WHITE");
    }

    @Override
    public boolean isDuplicatedName(final String roomName) {
        final String sql = "select count(*) from room where name = ?";
        final int count = jdbcTemplate.queryForObject(sql, Integer.class, roomName);
        return count > 0;
    }
}
